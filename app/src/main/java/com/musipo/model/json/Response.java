package com.musipo.model.json;


public class Response {

	private boolean result;
	private String accountId;
	private String serviceId;
	private String duration;


	private String vendorId;
	private String vendorLocalId;

	private String userCustomerId;
	private String userCustomerLocalId;

	private String customerId;
	private String customerLocalId;
	private String customerName;
	private String firstName;
	private String lastName;

	private String serviceCategoryId;
	private String categoryLocalId;

	private String planId;
	private String planLocalId;

	private String billId;
	private String billLocalId;
	private String billNo;

	private String subscriptionLocalId;
	private String subscriptionId;
	private String reminder_type;
	private String amount;
	private String business_name;
	private String service_charge;
	private String bill_date;
	private String service_type;
	private String startDate;
	private String endDate;


	private String amountReceiptLocalId;
	private String amountReceiptId;
	private String transactionId;

	private boolean syncStatus;
	private String status;
	private boolean alreadyAssociated;
	private String contactStatus;

	public String getReminder_type() {
		return reminder_type;
	}

	public void setReminder_type(String reminder_type) {
		this.reminder_type = reminder_type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getService_charge() {
		return service_charge;
	}

	public void setService_charge(String service_charge) {
		this.service_charge = service_charge;
	}

	public String getBill_date() {
		return bill_date;
	}


	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public String getContactStatus() {
		return contactStatus;
	}

	public void setContactStatus(String contactStatus) {
		this.contactStatus = contactStatus;
	}

	public boolean isAlreadyAssociated() {
		return alreadyAssociated;
	}
	public void setAlreadyAssociated(boolean alreadyAssociated) {
		this.alreadyAssociated = alreadyAssociated;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerLocalId() {
		return customerLocalId;
	}
	public void setCustomerLocalId(String customerLocalId) {
		this.customerLocalId = customerLocalId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getServiceCategoryId() {
		return serviceCategoryId;
	}
	public void setServiceCategoryId(String serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}
	public String getCategoryLocalId() {
		return categoryLocalId;
	}
	public void setCategoryLocalId(String categoryLocalId) {
		this.categoryLocalId = categoryLocalId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanLocalId() {
		return planLocalId;
	}
	public void setPlanLocalId(String planLocalId) {
		this.planLocalId = planLocalId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillLocalId() {
		return billLocalId;
	}
	public void setBillLocalId(String billLocalId) {
		this.billLocalId = billLocalId;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getSubscriptionLocalId() {
		return subscriptionLocalId;
	}
	public void setSubscriptionLocalId(String subscriptionLocalId) {
		this.subscriptionLocalId = subscriptionLocalId;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getAmountReceiptLocalId() {
		return amountReceiptLocalId;
	}
	public void setAmountReceiptLocalId(String amountReceiptLocalId) {
		this.amountReceiptLocalId = amountReceiptLocalId;
	}
	public String getAmountReceiptId() {
		return amountReceiptId;
	}
	public void setAmountReceiptId(String amountReceiptId) {
		this.amountReceiptId = amountReceiptId;
	}
	public boolean getSyncStatus() {
		return syncStatus;
	}
	public void setSyncStatus(boolean syncStatus) {
		this.syncStatus = syncStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorLocalId() {
		return vendorLocalId;
	}
	public void setVendorLocalId(String vendorLocalId) {
		this.vendorLocalId = vendorLocalId;
	}
	public String getUserCustomerId() {
		return userCustomerId;
	}
	public void setUserCustomerId(String userCustomerId) {
		this.userCustomerId = userCustomerId;
	}
	public String getUserCustomerLocalId() {
		return userCustomerLocalId;
	}
	public void setUserCustomerLocalId(String userCustomerLocalId) {
		this.userCustomerLocalId = userCustomerLocalId;
	}


	@Override
	public String toString() {
		return "Response{" +
				"result=" + result +
				", accountId='" + accountId + '\'' +
				", serviceId='" + serviceId + '\'' +
				", duration='" + duration + '\'' +
				", vendorId='" + vendorId + '\'' +
				", vendorLocalId='" + vendorLocalId + '\'' +
				", userCustomerId='" + userCustomerId + '\'' +
				", userCustomerLocalId='" + userCustomerLocalId + '\'' +
				", customerId='" + customerId + '\'' +
				", customerLocalId='" + customerLocalId + '\'' +
				", customerName='" + customerName + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", serviceCategoryId='" + serviceCategoryId + '\'' +
				", categoryLocalId='" + categoryLocalId + '\'' +
				", planId='" + planId + '\'' +
				", planLocalId='" + planLocalId + '\'' +
				", billId='" + billId + '\'' +
				", billLocalId='" + billLocalId + '\'' +
				", billNo='" + billNo + '\'' +
				", subscriptionLocalId='" + subscriptionLocalId + '\'' +
				", subscriptionId='" + subscriptionId + '\'' +
				", reminder_type='" + reminder_type + '\'' +
				", amount='" + amount + '\'' +
				", business_name='" + business_name + '\'' +
				", service_charge='" + service_charge + '\'' +
				", bill_date='" + bill_date + '\'' +
				", service_type='" + service_type + '\'' +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", amountReceiptLocalId='" + amountReceiptLocalId + '\'' +
				", amountReceiptId='" + amountReceiptId + '\'' +
				", transactionId='" + transactionId + '\'' +
				", syncStatus=" + syncStatus +
				", status='" + status + '\'' +
				", alreadyAssociated=" + alreadyAssociated +
				", contactStatus='" + contactStatus + '\'' +
				'}';
	}
}
