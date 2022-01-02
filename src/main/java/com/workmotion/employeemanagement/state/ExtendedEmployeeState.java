package com.workmotion.employeemanagement.state;

/**
 * An extended state to handle details of a state.
 */
public class ExtendedEmployeeState extends EmployeeState {
	private SecurityCheckStatus securityCheckStatus;
	private WorkPermitStatus workPermitStatus;

	// Can not be initialized outside package
	protected ExtendedEmployeeState(Status status) {
		super(status);
		this.securityCheckStatus = SecurityCheckStatus.SECURITY_CHECK_STARTED;
		this.workPermitStatus = WorkPermitStatus.WORK_PERMIT_CHECK_STARTED;
	}

	public SecurityCheckStatus getSecurityCheckStatus() {
		return this.securityCheckStatus;
	}

	public void setSecurityCheckStatus(SecurityCheckStatus securityCheckStatus) {
		this.securityCheckStatus = securityCheckStatus;
	}

	public WorkPermitStatus getWorkPermitStatus() {
		return this.workPermitStatus;
	}

	public void setWorkPermitStatus(WorkPermitStatus workPermitStatus) {
		this.workPermitStatus = workPermitStatus;
	}

	public boolean canChangeTo(EmployeeState employeeState) {
		if (!(employeeState instanceof ExtendedEmployeeState)) {
			return super.canChangeTo(employeeState) && this.securityCheckStatus.equals(SecurityCheckStatus.SECURITY_CHECK_FINISHED) && this.workPermitStatus.equals(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED);
		} else {
			ExtendedEmployeeState extendedState = (ExtendedEmployeeState) employeeState;
			return this.securityCheckStatus.canChangeTo(extendedState.getSecurityCheckStatus()) && this.workPermitStatus.canChangeTo(extendedState.getWorkPermitStatus()) && super.canChangeTo(extendedState);
		}
	}
}