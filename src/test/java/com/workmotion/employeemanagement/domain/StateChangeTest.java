package com.workmotion.employeemanagement.domain;

import com.workmotion.employeemanagement.state.EmployeeState;
import com.workmotion.employeemanagement.state.EmployeeStateProvider;
import com.workmotion.employeemanagement.state.ExtendedEmployeeState;
import com.workmotion.employeemanagement.state.SecurityCheckStatus;
import com.workmotion.employeemanagement.state.Status;
import com.workmotion.employeemanagement.state.WorkPermitStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StateChangeTest {
	private EmployeeState added;
	private EmployeeState inCheck;
	private EmployeeState approved;
	private EmployeeState active;

	public StateChangeTest() {
	}

	@BeforeEach
	public void beforeEach() {
		added = EmployeeStateProvider.getByStatus(Status.ADDED);
		inCheck = EmployeeStateProvider.getByStatus(Status.IN_CHECK);
		approved = EmployeeStateProvider.getByStatus(Status.APPROVED);
		active = EmployeeStateProvider.getByStatus(Status.ACTIVE);
	}

	@Test
	public void selfTransitionShouldBeAllowed() {
		Assertions.assertTrue(added.canChangeTo(added));
		Assertions.assertTrue(inCheck.canChangeTo(inCheck));
		Assertions.assertTrue(approved.canChangeTo(approved));
		Assertions.assertTrue(active.canChangeTo(active));
	}

	@Test
	public void transitionToOneStepFurtherShouldBeAllowed() {
		Assertions.assertTrue(added.canChangeTo(inCheck));
		Assertions.assertTrue(approved.canChangeTo(active));
	}

	@Test
	public void inCheckToApproveUnfinishedSecurityAndWorkPermitShouldNotBeAllowed() {
		EmployeeState inCheck = this.inCheck;
		Assertions.assertFalse(inCheck.canChangeTo(approved));
		ExtendedEmployeeState extendedState = (ExtendedEmployeeState)inCheck;
		extendedState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_STARTED);
		extendedState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED);
		Assertions.assertFalse(extendedState.canChangeTo(approved));
	}

	@Test
	public void inCheckToApproveUnfinishedSecurityShouldNotBeAllowed() {
		EmployeeState inCheck = this.inCheck;
		Assertions.assertFalse(inCheck.canChangeTo(approved));
		ExtendedEmployeeState extendedState = (ExtendedEmployeeState)inCheck;
		extendedState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_STARTED);
		extendedState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED);
		Assertions.assertFalse(extendedState.canChangeTo(approved));
	}

	@Test
	public void inCheckToApproveUnfinishedWorkPermitShouldNotBeAllowed() {
		EmployeeState inCheck = this.inCheck;
		Assertions.assertFalse(inCheck.canChangeTo(approved));
		ExtendedEmployeeState extendedState = (ExtendedEmployeeState)inCheck;
		extendedState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_FINISHED);
		extendedState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED);
		Assertions.assertFalse(extendedState.canChangeTo(approved));
	}

	@Test
	public void inCheckToApproveFinishedWorkPermitAndStatusShouldBeAllowed() {
		EmployeeState inCheck = this.inCheck;
		Assertions.assertFalse(inCheck.canChangeTo(approved));
		ExtendedEmployeeState extendedState = (ExtendedEmployeeState)inCheck;
		extendedState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_FINISHED);
		extendedState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED);
		Assertions.assertTrue(extendedState.canChangeTo(approved));
	}

	@Test
	public void transitionToMoreThanOneStepFurtherShouldNotBeAllowed() {
		Assertions.assertFalse(added.canChangeTo(approved));
		Assertions.assertFalse(added.canChangeTo(active));
		Assertions.assertFalse(inCheck.canChangeTo(active));
	}

	@Test
	public void backTransitionOneStepShouldNotBeAllowed() {
		Assertions.assertFalse(inCheck.canChangeTo(added));
		Assertions.assertFalse(approved.canChangeTo(inCheck));
		Assertions.assertFalse(active.canChangeTo(approved));
	}

	@Test
	public void backTransitionMoreThanOneStepShouldNotBeAllowed() {
		Assertions.assertFalse(approved.canChangeTo(added));
		Assertions.assertFalse(active.canChangeTo(inCheck));
		Assertions.assertFalse(active.canChangeTo(added));
	}
}
