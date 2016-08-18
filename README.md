Workflow
=======

A RESTful API for creating and executing workflows.

# Installation

TODO

# Testing

TODO

# Running

TODO

# API

### Create a new workflow

``POST /workflows``

Creates a new workflow, with a (integer) number of steps specified by a JSON body.

Example request body:

```
{
    "number_of_steps": 10
}
```

Response will be a 201 CREATED if successful, with a JSON body giving the new workflow's ID.

Example response:

```
{
    "workflow_id": "e02705b1-9523-4486-ab01-27a33094c83a"
}
```

### Create a new execution for a workflow

``POST /workflows/<workflow_id>/executions``

Creates a new workflow execution for an existing workflow.

Response will be 201 CREATED if successful, with a JSON body giving the execution ID, or 404 NOT FOUND if the workflow does not exist.

Example response:

```
{
    "workflow_execution_id": "e02705b1-9523-4486-ab01-27a33094c83a"
}
```

### Increment workflow execution

``PUT /workflows/<workflow_id>/executions/<workflow_execution_id>``

Increments the appropriate execution's current step by 1, if it has not reached the last step (i.e. if `current_step < workflow.number_of_steps - 1`).

Response will be 204 NO CONTENT if the step has been incremented, 400 BAD REQUEST if not, and 404 NOT FOUND if the workflow/execution do not exist.

### Fetch status of workflow execution

``GET /workflows/<workflow_id>/executions/<workflow_execution_id>``

Fetches the status of the selected execution - whether execution has finished or not.

Response will be 200 OK with a JSON body indicating whether execution has finished, or 404 NOT FOUND if the workflow/execution do not exist.

Example response:

```
{
    "finished": true
}
```
