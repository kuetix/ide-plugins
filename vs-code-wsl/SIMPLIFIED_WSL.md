# SimplifiedWSL (Simplified Workflow Specification Language)

## Overview

SimplifiedWSL is a streamlined syntax for the Workflow Specification Language (WSL) that removes the verbosity of traditional workflow declarations while maintaining the power and flexibility of the engine. It uses the `.swsl` file extension.

## Key Features

### Streamlined Syntax
- No `workflow name { }` wrappers required
- Direct action flow with `->` (forward flow) operator
- Error binding with `<-` operator
- Terminal state with `.` operator
- Optional module declarations (can derive from filename)

### Full Engine Compatibility
- Converts to standard WSL AST internally
- Uses the same execution engine
- Shares all features with regular WSL
- Support for workflow types (feature, solution, workflow, custom)

### Simplified Constructs
- `def` keyword for reusable action definitions
- Inline error handling without explicit error states
- Action chaining with `->` for sequential execution
- Action aliasing with `as` keyword

## Syntax Comparison

### Traditional WSL:
```wsl
module example

const {
    msg: "Hello"
}

workflow main {
    start: CheckSystem
    
    state CheckSystem {
        action speak/Say(on: "message", v: $constants.msg)
        on success -> ProcessResult
        on error -> ErrorHandler
    }
    
    state ErrorHandler {
        action errors/OnAnyError()
        end fail
    }
    
    state ProcessResult {
        action common/Response(message: "Done")
        end ok
    }
}
```

### SimplifiedWSL:
```swsl
module example

feature  # Optional: specify workflow type

const {
    msg: "Hello"
}

def errors.OnAnyError() as errorHandler -> .

speak.Say(on: "message", v: $constants.msg) <- errorHandler -> common.Response(message: "Done") -> .
```

## Core Syntax Elements

### 1. Module Declaration (Optional)
```swsl
module my_workflow
```
- If omitted with filename: derives name from filename (e.g., `payment.swsl` → module `payment`)
- If omitted without filename: defaults to `main`

### 2. Workflow Type Declaration (Optional)
```swsl
feature                    # Type only, name defaults to "main"
feature payment_processor  # Type with explicit name
solution                   # Solution type
workflow                   # Explicit workflow type
microservice api_gateway   # Custom type
```

### 3. Constants Block (Optional)
```swsl
const {
    timeout: 5000,
    retryCount: 3,
    config: {
        nested: "values",
        array: [1, 2, 3]
    }
}
```

### 4. Action Definitions with `def` (Optional)
```swsl
def errors.LogError(level: "error") as errorHandler -> .
def common.Validate(strict: true) as validator -> .
```
- Defines reusable actions that can be referenced
- Not executed as separate workflow states
- Inlined at the point where they're referenced

### 5. Action Flows
```swsl
// Simple flow
action.Call() -> .

// Flow with error handler
action.Call() <- errorHandler -> .

// Chained flow
action.First() -> action.Second() -> action.Third() -> .

// Flow with alias
action.Call(param: "value") as result -> .

// Using aliased results
action.Process(data: $result) -> .
```

### 6. Hierarchical Calls
```swsl
// Call a workflow
workflow:validate_payment() -> .

// Call a feature
feature:payment_processing() -> .

// Call actions
payment.Process() -> .
```

## Operators

### `->` Forward Flow Operator
Chains actions sequentially. Execution continues to the next action on success.

```swsl
action.First() -> action.Second() -> .
```

### `<-` Error Binding Operator
Binds an error handler to an action. If the action fails, the error handler is executed.

```swsl
action.Call() <- errorHandler -> .
```

### `.` Terminal Operator
Marks the end of a workflow. The workflow completes at this point.

```swsl
action.FinalStep() -> .
```

### `as` Aliasing Keyword
Creates an alias for an action result that can be referenced later.

```swsl
action.GetData() as result ->
action.Process(data: $result) -> .
```

## Variables

### Constants
Reference constants defined in the const block:
```swsl
$constants.timeout
$constants.config.nested
```

### Context Variables
Reference values from the workflow execution context:
```swsl
$context.userId
$context.sessionData
```

## Workflow Types

SimplifiedWSL supports hierarchical workflow types:

### Solution
Top-level orchestration that can call features, workflows, and actions:
```swsl
solution payment_solution

feature:process_payment() -> 
feature:send_notification() -> .
```

### Feature
Mid-level orchestration that can call workflows and actions:
```swsl
feature payment_processing

workflow:validate() ->
workflow:process() -> .
```

### Workflow
Basic workflow that calls actions:
```swsl
workflow payment_validation

payment.ValidateAmount() ->
payment.ValidateMethod() -> .
```

### Custom Types
You can define custom workflow types:
```swsl
microservice api_gateway

// workflow implementation
```

## Complete Example

```swsl
module payment_feature

feature payment_processing

const {
    timeout: 5000,
    retryCount: 3,
    maxAmount: 10000
}

def errors.LogError(level: "error") as errorHandler -> .
def errors.HandleCritical(severity: "critical") as criticalError -> .

// Initialize payment
context.Set(key: "payment_id", value: "PAY-123") ->

// Validate payment
payment.ValidateAmount(max: $constants.maxAmount) <- errorHandler ->
payment.ValidateMethod() <- errorHandler ->

// Process payment with retries
payment.ProcessTransaction(
    id: $context.payment_id,
    retries: $constants.retryCount,
    timeout: $constants.timeout
) <- criticalError ->

// Send receipt
payment.SendReceipt(id: $context.payment_id) <- errorHandler -> .
```

## Best Practices

### When to Use SimplifiedWSL
- Building simple linear or branching workflows
- Rapid prototyping of action flows
- Workflow is primarily a sequence of actions
- Want to reduce visual complexity
- Need quick scaffolding with templates

### When to Use Regular WSL
- Need multiple named workflows in one file
- Require advanced state management
- Need complex conditional logic with `when` expressions
- Want explicit control over state names
- Need parameterized states

### Error Handling
Always define error handlers for critical operations:
```swsl
def errors.HandleCritical() as criticalHandler -> .

critical.Operation() <- criticalHandler -> .
```

### Reusable Definitions
Use `def` for actions that are used multiple times:
```swsl
def common.Validate(strict: true) as validator -> .

data.Input() -> validator ->
data.Process() -> validator -> .
```

### Clear Naming
Use descriptive names for modules, workflows, and aliases:
```swsl
module user_authentication

feature user_login

payment.Authenticate(user: $context.username) as authResult ->
```

## File Extension

SimplifiedWSL files must use the `.swsl` extension to be recognized by the parser and IDE plugins.

## IDE Support

Both VS Code and JetBrains IDEs provide full support for SimplifiedWSL:
- Syntax highlighting
- Code completion
- Snippets for common patterns
- File type recognition

## See Also

- [Hierarchical Execution Guide](./HIERARCHICAL_EXECUTION.md) - Learn about workflow orchestration
- [Example Files](../examples/hierarchical/) - Working examples of SimplifiedWSL
- Regular WSL documentation - For advanced features
