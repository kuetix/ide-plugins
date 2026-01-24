# Hierarchical Execution in WSL

## Overview

WSL supports hierarchical execution, allowing workflows to call other workflows in a structured, layered architecture. This enables better organization, reusability, and maintainability of complex workflow systems.

## Workflow Type Hierarchy

```
Solution
  ├─> Can call: features, workflows, actions
  └─> Shares WorkerSessionContext
        ↓
      Feature
        ├─> Can call: workflows, actions
        └─> Shares WorkerSessionContext
              ↓
            Workflow
              ├─> Can call: actions
              └─> Shares WorkerSessionContext
```

## Workflow Types

### Solution
**Purpose:** Top-level orchestration of business solutions

**Can Call:** Features, workflows, and actions

**Use Cases:**
- Complete end-to-end business processes
- Multi-feature orchestration
- System-level integration

**Example (SimplifiedWSL):**
```swsl
module ecommerce_solution

solution complete_order

feature:payment_processing() ->
feature:inventory_management() ->
feature:shipping_notification() -> .
```

**Example (Regular WSL):**
```wsl
module ecommerce_solution

solution complete_order {
    start: ProcessPayment
    
    state ProcessPayment {
        action feature:payment_processing()
        on success -> ManageInventory
    }
    
    state ManageInventory {
        action feature:inventory_management()
        on success -> NotifyShipping
    }
    
    state NotifyShipping {
        action feature:shipping_notification()
        end ok
    }
}
```

### Feature
**Purpose:** Mid-level orchestration of related functionality

**Can Call:** Workflows and actions

**Use Cases:**
- Feature-level business logic
- Coordinating multiple workflows
- Reusable feature components

**Example (SimplifiedWSL):**
```swsl
module payment_processing

feature payment_feature

workflow:validate_payment() ->
workflow:process_payment() ->
workflow:send_receipt() -> .
```

**Example (Regular WSL):**
```wsl
module payment_processing

feature payment_feature {
    start: Validate
    
    state Validate {
        action workflow:validate_payment()
        on success -> Process
    }
    
    state Process {
        action workflow:process_payment()
        on success -> SendReceipt
    }
    
    state SendReceipt {
        action workflow:send_receipt()
        end ok
    }
}
```

### Workflow
**Purpose:** Basic unit of workflow execution

**Can Call:** Actions

**Use Cases:**
- Specific task execution
- Sequential action processing
- Reusable workflow components

**Example (SimplifiedWSL):**
```swsl
module validate_payment

workflow

payment.ValidateAmount() ->
payment.ValidateMethod() ->
payment.CheckFraud() -> .
```

**Example (Regular WSL):**
```wsl
module validate_payment

workflow main {
    start: ValidateAmount
    
    state ValidateAmount {
        action payment.ValidateAmount()
        on success -> ValidateMethod
    }
    
    state ValidateMethod {
        action payment.ValidateMethod()
        on success -> CheckFraud
    }
    
    state CheckFraud {
        action payment.CheckFraud()
        end ok
    }
}
```

## Hierarchical Call Syntax

### SimplifiedWSL
Use the colon (`:`) syntax to make hierarchical calls:

```swsl
// Call a solution
solution:order_processing() -> .

// Call a feature
feature:payment_feature() -> .

// Call a workflow
workflow:validate_payment() -> .

// Call an action (no prefix needed)
payment.Process() -> .
```

### Regular WSL
Use the colon (`:`) syntax in action declarations:

```wsl
state CallFeature {
    action feature:payment_feature()
    on success -> NextState
}

state CallWorkflow {
    action workflow:validate_payment()
    on success -> FinalState
}
```

## Shared Context

All workflow types share the same `WorkerSessionContext`, which allows data to flow between hierarchical calls.

### Setting Context Values

**SimplifiedWSL:**
```swsl
context.Set(key: "payment_amount", value: 100.00) ->
context.Set(key: "user_id", value: "USER-123") -> .
```

**Regular WSL:**
```wsl
state SetContext {
    action context.Set(key: "payment_amount", value: 100.00)
    on success -> NextState
}
```

### Accessing Context Values

**SimplifiedWSL:**
```swsl
payment.Process(amount: $context.payment_amount) ->
notification.Send(userId: $context.user_id) -> .
```

**Regular WSL:**
```wsl
state ProcessPayment {
    action payment.Process(amount: $context.payment_amount)
    on success -> SendNotification
}
```

### Context Flow Example

```swsl
// Solution sets initial context
solution order_solution

context.Set(key: "order_id", value: "ORD-456") ->

// Feature accesses and adds to context
feature:payment_processing(orderId: $context.order_id) ->

// Inside payment_processing feature:
// workflow:validate_payment() can access $context.order_id
// workflow:process_payment() can access all context values
```

## Complete Hierarchical Example

### Solution Level
**File:** `ecommerce_solution.swsl`
```swsl
module ecommerce_solution

solution complete_order

const {
    environment: "production"
}

def errors.HandleCritical(severity: "critical") as critical -> .

// Initialize order context
context.Set(key: "order_id", value: "ORD-123") ->
context.Set(key: "customer_id", value: "CUST-456") ->

// Process payment
feature:payment_processing(orderId: $context.order_id) <- critical ->

// Manage inventory
feature:inventory_management(orderId: $context.order_id) <- critical ->

// Send notifications
feature:notification_service(customerId: $context.customer_id) <- critical -> .
```

### Feature Level
**File:** `payment_processing.swsl`
```swsl
module payment_processing

feature payment_feature

const {
    timeout: 5000,
    retryCount: 3
}

def errors.LogError(level: "error") as error -> .

// Validate payment
workflow:validate_payment(timeout: $constants.timeout) <- error ->

// Process payment
workflow:process_payment(retries: $constants.retryCount) <- error ->

// Send receipt
workflow:send_receipt() <- error -> .
```

### Workflow Level
**File:** `validate_payment.swsl`
```swsl
module validate_payment

workflow

const {
    minAmount: 1.00,
    maxAmount: 10000.00
}

def errors.OnAnyError() as error -> .

// Validate amount
payment.ValidateAmount(
    min: $constants.minAmount,
    max: $constants.maxAmount,
    orderId: $context.order_id
) <- error ->

// Validate method
payment.ValidateMethod(orderId: $context.order_id) <- error -> .
```

## Best Practices

### 1. Use Appropriate Workflow Types
- **Solutions:** For complete business processes involving multiple features
- **Features:** For related functionality that coordinates multiple workflows
- **Workflows:** For specific tasks with sequential actions

### 2. Keep Hierarchy Shallow
Avoid deeply nested hierarchies. Generally, 3 levels (solution → feature → workflow) is sufficient.

### 3. Share Context Thoughtfully
- Set context at the highest appropriate level
- Use clear, descriptive context keys
- Document what context values are expected

### 4. Error Handling at Each Level
Define appropriate error handlers at each hierarchical level:

```swsl
// Solution level - critical errors
def errors.HandleCritical(severity: "critical") as critical -> .

// Feature level - business errors
def errors.HandleBusinessError(level: "warning") as business -> .

// Workflow level - technical errors
def errors.LogError(level: "error") as technical -> .
```

### 5. Use Constants for Configuration
Define constants at the appropriate level and pass them down:

```swsl
// Feature level constants
const {
    timeout: 5000,
    retryCount: 3
}

// Pass to workflows
workflow:process_payment(
    timeout: $constants.timeout,
    retries: $constants.retryCount
) -> .
```

## Testing Hierarchical Workflows

### Unit Testing
Test each workflow independently:
- Test workflows with mock actions
- Test features with mock workflows
- Test solutions with mock features

### Integration Testing
Test the complete hierarchy:
- Verify context flows correctly through all levels
- Verify error handling propagates appropriately
- Verify all hierarchical calls execute in order

### Example Test Structure
```
tests/
  ├── unit/
  │   ├── workflows/
  │   │   ├── test_validate_payment.swsl
  │   │   └── test_process_payment.swsl
  │   └── features/
  │       └── test_payment_processing.swsl
  └── integration/
      └── test_complete_order_solution.swsl
```

## Common Patterns

### 1. Feature Composition
Combine multiple features to create a solution:
```swsl
solution complete_process

feature:data_ingestion() ->
feature:data_processing() ->
feature:data_output() -> .
```

### 2. Workflow Reuse
Reuse workflows across multiple features:
```swsl
feature data_ingestion

workflow:validate_data() ->
workflow:load_data() -> .

feature data_processing

workflow:validate_data() ->  // Reuse validation
workflow:transform_data() -> .
```

### 3. Error Recovery Chain
Handle errors at different levels:
```swsl
// Workflow level - retry
workflow:process_data() <- retry ->

// Feature level - fallback
feature:primary_service() <- fallback_service ->

// Solution level - critical failure
solution:main_process <- critical_error -> .
```

## See Also

- [SimplifiedWSL Guide](./SIMPLIFIED_WSL.md) - Learn SimplifiedWSL syntax
- [Example Files](../examples/hierarchical/) - Working hierarchical examples
- Regular WSL documentation - For advanced features
