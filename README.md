# ATM Project - Part II (Re-engineering)

## TODO List

### Enhancements to Implement

- [ ] **Adjust Cash Withdrawal Options**
  - Modify the cash withdrawal options to allow only multiples of HKD 100, HKD 500, and HKD 1000.

- [ ] **Introduce Specific Account Types**
  - Create a `SavingsAccount` subclass with:
    - [ ] Interest rate attribute (default value: 0.5% per annum).
  - Create a `ChequeAccount` subclass with:
    - [ ] Limit per cheque attribute (default value: HK$10,000).

- [ ] **Remove Deposit Functionality**
  - Eliminate the existing deposit function from the current system.

- [ ] **Implement Fund Transfer Function**
  - Add a new function to facilitate transferring funds from one bank account to another.

### Class Diagram
- [ ] **Reconstruct Class Diagram**
  - Update the class diagram to reflect the modified version of the prototype, including new account types and functionalities.

### Testing
- [ ] Write unit tests for:
  - [ ] Cash withdrawal functionality.
  - [ ] Savings account interest calculation.
  - [ ] Cheque account limit enforcement.
  - [ ] Fund transfer functionality.

### Documentation
- [ ] Update README with detailed instructions on new features.
- [ ] Document code changes with comments and Javadoc.

### Future Considerations
- [ ] Evaluate potential enhancements for user experience.
- [ ] Consider adding support for multiple currencies in future iterations.
