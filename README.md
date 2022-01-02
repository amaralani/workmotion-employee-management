# Employee Management

[![CircleCI](https://img.shields.io/circleci/build/github/amaralani/workmotion-employee-management)](https://circleci.com/gh/amaralani/workmotion-employee-management)

This codebase is my implementation of [WorkMotion Senior Challenge](https://github.com/workmotion/backend-challenge/blob/main/SENIOR_CHALLENGE.md).

### Specifications

* Spring Boot 2.6.4
* Lombok
* No database implementation, keeps data in memory (although extensible).
* Docker
* docker-compose
* Unit tests and Integration tests
* OpenAPI 3 specification

### Notes

* State transfer is done using an enum `Status` and two classes `EmployeState` and `ExtendedEmployeeState`.
* Each of `ADDED`, `IN-CHECK`, `APPROVED`, `ACTIVE` is considered a *status* and a  `EmployeState` or `ExtendedEmployeeState` is assigneded to them.
* `ADDED`, `APPROVED`, `ACTIVE` are assigned a `EmployeState`.
* `IN-CHECK` is assigned a `ExtendedEmployeeState`.
* `ExtendedEmployeeState` has its own properties: *securityCheckStatus* and *workPermitStatus*. 
* Spring State Machine looks like a very good framework for this task but since I have never used it before I preferred going with a custom implementation to avoid giving wrong impression. 
