# ATM Project due 27 OCT

## TODO List

### Enhancements to Implement

- [X] **Adjust Cash Withdrawal Options**
    - Modify the cash withdrawal options to allow only multiples of HKD 100, HKD 500, and HKD 1000.

- [ ] **Introduce Specific Account Types**
    - Create a `SavingsAccount` subclass with: 85% done
        - [X] Interest rate attribute (default value: 0.5% per annum).
            - [ ] Show Interest rate in account details?
    - Create a `ChequeAccount` subclass with:
        - [ ] Limit per cheque attribute (default value: HK$10,000).
            - [ ] Decline transfer when cheque account over limit?

- [X] **Remove Deposit Functionality**
    - Eliminate the existing deposit function from the current system.

- [ ] **Implement Fund Transfer Function** 85% done
    - Add a new function to facilitate transferring funds from one bank account to another.
    - ??? Should funds be transfered to total balance or available balance (Currently transfer to total balance)???
    - Code refactoring

### Class Diagram

- [ ] **Reconstruct Class Diagram**
    - Update the class diagram to reflect the modified version of the prototype, including new account types and
      functionalities.

### Testing

can use AI to generate Test cases

- [ ] Write unit tests for:
    - [ ] Cash withdrawal functionality.
    - [ ] Savings account interest calculation.
    - [ ] Cheque account limit enforcement.
    - [ ] Fund transfer functionality.

### Documentation

- [ ] Update README with detailed instructions on new features.
- [ ] Document code changes with comments and Javadoc.
