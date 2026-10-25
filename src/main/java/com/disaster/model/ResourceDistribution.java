package com.disaster.model;

import java.sql.Timestamp;

/**
 * ResourceDistribution domain model.
 * Records distribution transactions from warehouse stock to shelters or rescue units.
 */
public class ResourceDistribution extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long resourceId;
    private String resourceName;
    private String resourceUnit;
    private int quantity;
    private String destinationType; // SHELTER, RESCUE_MISSION, DIRECT_COMMUNITY
    private String destinationName;
    private Long shelterId;
    private Long requestId;
    private Long distributedBy;
    private String distributedByName;
    private String recipientContact;
    private Timestamp distributionDate;
    private String remarks;

    public ResourceDistribution() {
        super();
        this.distributionDate = new Timestamp(System.currentTimeMillis());
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUnit() {
        return resourceUnit;
    }

    public void setResourceUnit(String resourceUnit) {
        this.resourceUnit = resourceUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public Long getShelterId() {
        return shelterId;
    }

    public void setShelterId(Long shelterId) {
        this.shelterId = shelterId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getDistributedBy() {
        return distributedBy;
    }

    public void setDistributedBy(Long distributedBy) {
        this.distributedBy = distributedBy;
    }

    public String getDistributedByName() {
        return distributedByName;
    }

    public void setDistributedByName(String distributedByName) {
        this.distributedByName = distributedByName;
    }

    public String getRecipientContact() {
        return recipientContact;
    }

    public void setRecipientContact(String recipientContact) {
        this.recipientContact = recipientContact;
    }

    public Timestamp getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(Timestamp distributionDate) {
        this.distributionDate = distributionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

