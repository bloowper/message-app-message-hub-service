# Genneral info
Service responsible for sending messages to users.

# Tech info

## Rabbitmq
message dispatching is based on rabbitmq
by creating a queue per user connection and then binding to message exchange
### Exchanges
| name            |     type     |  durability  | auto delete  | internal |
|:----------------|:------------:|:------------:|:------------:|:--------:|
| tx.user.message |    topic     |  transient   |      no      |    no    |

tx.user.message - bus for user messages
