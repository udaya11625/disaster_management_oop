/**
 * Chart.js Visualizations for Disaster Management Dashboard
 */

document.addEventListener('DOMContentLoaded', function () {
    const reportsCanvas = document.getElementById('reportsByTypeChart');
    const requestsCanvas = document.getElementById('requestsByStatusChart');
    const shelterCanvas = document.getElementById('shelterOccupancyChart');

    if (!reportsCanvas && !requestsCanvas && !shelterCanvas) {
        return; // No charts on this page
    }

    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 2));
    const apiUrl = (contextPath.startsWith('/disaster-management') ? contextPath : '') + '/api/charts/data';

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            // 1. Reports by Disaster Type (Doughnut Chart)
            if (reportsCanvas && data.reportsByType) {
                const labels = Object.keys(data.reportsByType);
                const values = Object.values(data.reportsByType);
                const colors = ['#0288D1', '#D32F2F', '#FFA000', '#7B1FA2', '#E64A19', '#455A64', '#388E3C'];

                new Chart(reportsCanvas, {
                    type: 'doughnut',
                    data: {
                        labels: labels,
                        datasets: [{
                            data: values,
                            backgroundColor: colors.slice(0, labels.length),
                            borderWidth: 2,
                            borderColor: '#FFFFFF'
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: { position: 'bottom' }
                        }
                    }
                });
            }

            // 2. Requests by Status (Bar Chart)
            if (requestsCanvas && data.requestsByStatus) {
                const labels = ['PENDING', 'ASSIGNED', 'IN_PROGRESS', 'RESOLVED'];
                const values = labels.map(l => data.requestsByStatus[l] || 0);

                new Chart(requestsCanvas, {
                    type: 'bar',
                    data: {
                        labels: ['Pending Dispatch', 'Team Assigned', 'In Progress', 'Resolved'],
                        datasets: [{
                            label: 'Assistance Requests',
                            data: values,
                            backgroundColor: ['#FFA000', '#0288D1', '#0B1B3D', '#2E7D32'],
                            borderRadius: 6
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: { stepSize: 1 }
                            }
                        },
                        plugins: {
                            legend: { display: false }
                        }
                    }
                });
            }

            // 3. Shelter Capacity vs Occupancy
            if (shelterCanvas && data.shelterOccupancy) {
                const names = data.shelterOccupancy.map(s => s.name.length > 20 ? s.name.substring(0, 18) + '...' : s.name);
                const capacities = data.shelterOccupancy.map(s => s.totalCapacity);
                const occupancies = data.shelterOccupancy.map(s => s.currentOccupancy);

                new Chart(shelterCanvas, {
                    type: 'bar',
                    data: {
                        labels: names,
                        datasets: [
                            {
                                label: 'Occupancy',
                                data: occupancies,
                                backgroundColor: '#D32F2F',
                                borderRadius: 4
                            },
                            {
                                label: 'Total Capacity',
                                data: capacities,
                                backgroundColor: '#CBD5E1',
                                borderRadius: 4
                            }
                        ]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            x: { stacked: false },
                            y: { beginAtZero: true }
                        },
                        plugins: {
                            legend: { position: 'bottom' }
                        }
                    }
                });
            }
        })
        .catch(err => {
            console.error('Could not load chart analytics:', err);
        });
});

