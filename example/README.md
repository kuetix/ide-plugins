# WSL and SimplifiedWSL Examples

This directory contains example workflows demonstrating both WSL (Workflow Specific Language) and SimplifiedWSL syntax.

> **Note:** The SimplifiedWSL examples in the `hierarchical/` directory use demonstration modules (such as `payment`, `notification`, `context`, `services/common`, `errors`, `logger`) that are not defined in `modules.json`. These examples are designed to showcase SimplifiedWSL syntax and patterns, not to be executable workflows. For examples using the actual modules defined in `modules.json` (like `auth/login`, `db/query`, `http.request`, `response.json`, `validation.rules`), see the traditional WSL examples.

## Directory Structure

```
example/
├── modules.json              # Module definitions for workflow actions
└── workflows/
    ├── wsl_hello_world/      # Traditional WSL examples
    │   └── example.wsl       # Hello world workflow in WSL syntax
    ├── swsl_hello_world/     # SimplifiedWSL examples
    │   └── example.swsl      # Hello world workflow in SimplifiedWSL syntax
    ├── workflow/             # Traditional WSL workflow examples
    │   ├── workflow.json     # Workflow metadata
    │   └── login.wsl         # Login workflow in WSL syntax
    └── hierarchical/         # SimplifiedWSL hierarchical execution examples
        ├── solution_example.swsl         # Solution-level orchestration
        ├── feature_payment.swsl          # Payment feature
        ├── feature_notification.swsl     # Notification feature
        ├── workflow_validate.swsl        # Payment validation workflow
        ├── workflow_process.swsl         # Payment processing workflow
        └── workflow_notify.swsl          # Customer notification workflow
```

## WSL vs SimplifiedWSL

### Traditional WSL (.wsl)
Traditional WSL uses a verbose, state-machine based syntax with explicit state declarations:

```wsl
workflow example {
  start: InitialState
  
  state InitialState {
    action module.Method()
    on success -> NextState
    on error -> ErrorState
  }
  
  state NextState {
    action module.AnotherMethod()
    end ok
  }
}
```

### SimplifiedWSL (.swsl)
SimplifiedWSL uses a streamlined syntax with operators for flow control:

```swsl
module example

workflow example

// Simple linear flow
module.Method() ->
module.AnotherMethod() -> .
```

## SimplifiedWSL Syntax

### Key Operators
- `->` : Forward flow operator (sequential execution)
- `<-` : Error binding operator (error handling)
- `.`  : Terminal operator (end of workflow)

### Keywords
- `module` : Module declaration
- `import` : Import statement
- `const` : Constants block
- `def` : Define reusable action
- `workflow` : Workflow type declaration
- `feature` : Feature workflow type
- `solution` : Solution workflow type

### Hierarchical Execution

SimplifiedWSL supports hierarchical workflow execution with three levels:

1. **Solution** - Top-level orchestration
2. **Feature** - Mid-level feature grouping
3. **Workflow** - Low-level task execution

Call workflows hierarchically:
```swsl
solution orchestrator

// Call a feature
feature:payment_feature() ->

// Call a workflow directly
workflow:validate_payment() -> .
```

## Examples

### Simple Hello World (swsl_hello_world/example.swsl)
Demonstrates basic SimplifiedWSL syntax with:
- Module declaration
- Workflow type declaration
- Constants block
- Simple action flow
- Terminal operator

### Hierarchical Examples (hierarchical/)

The hierarchical examples demonstrate a complete payment processing system:

1. **solution_example.swsl** - Top-level solution that:
   - Sets up payment context
   - Calls payment feature
   - Calls notification feature
   - Handles critical errors

2. **feature_payment.swsl** - Payment feature that:
   - Validates payment
   - Processes payment
   - Sends receipt

3. **feature_notification.swsl** - Notification feature that:
   - Sends notifications to customers

4. **workflow_validate.swsl** - Validates payment details
5. **workflow_process.swsl** - Processes the actual payment
6. **workflow_notify.swsl** - Sends customer notifications

## Using These Examples

### With JetBrains IDEs
1. Install the WSL Language Support plugin
2. Open any `.wsl` or `.swsl` file
3. Get automatic syntax highlighting
4. Use IDE features like code structure, commenting, and brace matching

### With VS Code
1. Install the WSL Language Support extension
2. Open any `.wsl` or `.swsl` file
3. Get automatic syntax highlighting
4. Use code snippets (type `swsl-` and press Tab)

## Learn More

- See `SIMPLIFIED_WSL.md` in the plugin directories for complete syntax reference
- See `HIERARCHICAL_EXECUTION.md` for detailed hierarchical execution patterns
- Refer to `modules.json` for available actions and methods
