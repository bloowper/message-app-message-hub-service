# Message Hub Service: Efficient Message Dispatching for User Communications

## General Info
This microservice is responsible for dispatching messages to users, facilitating seamless communication within the network.

## Technical Architecture and System Profiles

### Profiles
| Name                  | Description |
|:----------------------|:-------------|
| messageChoreographer  | Activates the message choreographer, which is crucial for routing messages to users based on their channel subscriptions. Without this, messages would be broadcast to all users indiscriminately. |
| channelInformation    | Enables the channel information module, providing vital data about user channels. Lacking this profile, channel assignments must be manually set at startup using a stub method. |

### RabbitMQ
Our service leverages RabbitMQ for efficient message dispatching. It achieves this by creating a unique queue for each user connection and binding these queues to a central message exchange.

### Rabbitmq Exchanges
| name            |     type     |  durability  | auto delete  | internal |
|:----------------|:------------:|:------------:|:------------:|:--------:|
| tx.user.message |    topic     |  transient   |      no      |    no    |

tx.user.message - bus for user messages

