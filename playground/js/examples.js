// WSL and SimplifiedWSL Examples

const examples = {
    hello_world: {
        name: "Hello World (SWSL)",
        language: "swsl",
        code: `// SimplifiedWSL Example: Hello World
module example

workflow hello_world

const {
    event: "greet",
    message: "Hello, World from SimplifiedWSL!",
    version: "1.0.0"
}

// Simple greeting workflow using SimplifiedWSL syntax
converse/speak.Say(on: "message", response: $constants.message, statusCode: 200) ->
services/common.Response(message: "Greeting sent successfully", status: "ok") -> .
`
    },

    payment_feature: {
        name: "Payment Feature (SWSL)",
        language: "swsl",
        code: `// SimplifiedWSL Payment Feature Example
module payment

feature payment_processor

const {
    timeout: 30000,
    currency: "USD",
    min_amount: 1.00
}

// Payment processing feature with validation
workflow:validate_payment() ->
workflow:process_payment() ->
workflow:send_receipt() -> .
`
    },

    solution_example: {
        name: "Solution Example (SWSL)",
        language: "swsl",
        code: `// SimplifiedWSL Solution Example
module payment_solution

solution payment_orchestrator

const {
    version: "1.0.0",
    max_retries: 3
}

// Solution-level orchestration
context.Setup() ->
feature:payment_feature() ->
feature:notification_feature() <- 
  error.CriticalError() -> .
`
    },

    login_workflow: {
        name: "Login Workflow (WSL)",
        language: "wsl",
        code: `// Traditional WSL Login Workflow Example
workflow login {
  start: ValidateCredentials
  
  state ValidateCredentials {
    action validation.rules.ValidateLoginCredentials(
      username: $input.username,
      password: $input.password
    )
    on success -> AuthenticateUser
    on error -> LoginFailed
  }
  
  state AuthenticateUser {
    action auth/login.Authenticate(
      username: $input.username,
      password: $input.password
    )
    on success -> FetchUserData
    on error -> LoginFailed
  }
  
  state FetchUserData {
    action db/query.GetUserProfile(
      userId: $state.userId
    )
    on success -> GenerateToken
    on error -> LoginFailed
  }
  
  state GenerateToken {
    action auth/login.GenerateJWT(
      userId: $state.userId,
      expiresIn: "24h"
    )
    on success -> ReturnSuccess
    on error -> LoginFailed
  }
  
  state ReturnSuccess {
    action response.json.SendResponse(
      status: 200,
      body: {
        success: true,
        token: $state.token,
        user: $state.userProfile
      }
    )
    end ok
  }
  
  state LoginFailed {
    action response.json.SendResponse(
      status: 401,
      body: {
        success: false,
        error: "Invalid credentials"
      }
    )
    end error
  }
}
`
    },

    workflow_validate: {
        name: "Validate Workflow (SWSL)",
        language: "swsl",
        code: `// SimplifiedWSL Validation Workflow
module payment

workflow validate_payment

const {
    min_amount: 1.00,
    max_amount: 10000.00
}

// Validate payment details
payment.ValidateAmount(
    amount: $context.payment.amount,
    min: $constants.min_amount,
    max: $constants.max_amount
) ->
payment.ValidateCard(
    cardNumber: $context.payment.cardNumber
) ->
payment.ValidateExpiry(
    expiry: $context.payment.expiry
) <- 
  errors.ValidationError() -> .
`
    },

    action_chain: {
        name: "Action Chain (SWSL)",
        language: "swsl",
        code: `// SimplifiedWSL Action Chaining Example
module example

workflow action_chain

// Define reusable actions
def validateInput = validation.Check(input: $context.data)
def processData = processor.Transform(data: $context.data)
def saveResult = db.Save(result: $context.processed)

// Chain actions together
$validateInput ->
$processData ->
$saveResult <- 
  error.HandleError() -> .
`
    },

    hierarchical_call: {
        name: "Hierarchical Call (SWSL)",
        language: "swsl",
        code: `// SimplifiedWSL Hierarchical Execution
module orchestrator

solution payment_solution

const {
    retry_count: 3,
    timeout: 30000
}

// Call workflows at different levels
context.Initialize() ->

// Call feature-level workflow
feature:payment_processing() ->

// Call workflow-level directly
workflow:send_notification() ->

// Call another solution
solution:audit_logging() -> .
`
    },

    error_handling: {
        name: "Error Handling (SWSL)",
        language: "swsl",
        code: `// SimplifiedWSL Error Handling Example
module example

workflow error_handling_demo

// Action with error handler
action.RiskyOperation() <- 
  error.LogError() ->
  error.NotifyAdmin() ->
  action.Fallback() -> .

// Multiple error paths
action.Primary() <- (
  error.Type1() -> action.Recover1(),
  error.Type2() -> action.Recover2(),
  error.Default() -> action.FallbackAll()
) -> .
`
    }
};

// Get example by key
function getExample(key) {
    return examples[key] || null;
}

// Get all example keys
function getExampleKeys() {
    return Object.keys(examples);
}

// Get example list for dropdown
function getExampleList() {
    return Object.keys(examples).map(key => ({
        key,
        name: examples[key].name,
        language: examples[key].language
    }));
}
