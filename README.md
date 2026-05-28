# OmniSupport AI - Cloud-Native Intelligent Support Platform

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/java-17-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.14-brightgreen.svg)
![Docker](https://img.shields.io/badge/Docker-Supported-blue.svg)

**OmniSupport AI** is an enterprise-grade, cloud-native SaaS platform for intelligent support ticket management and customer issue resolution. Built with Spring Boot microservices architecture, it delivers scalable, real-time support operations with AI-inspired ticket prioritization and comprehensive analytics.

## 📋 Project Overview

OmniSupport AI is designed for modern support operations teams that require:
- **Multi-channel ticket management** - Unified support ticket platform
- **Intelligent ticket routing** - AI-inspired priority engine for automatic categorization
- **Real-time notifications** - Event-driven architecture with Kafka
- **Role-based access control** - Support for ADMIN, AGENT, and CUSTOMER roles
- **Analytics & reporting** - Comprehensive support metrics and performance dashboards
- **Scalable microservices** - Independent service deployment and scaling
- **Cloud-ready** - Docker containerization and orchestration support

## 🏗️ Architecture Overview

### Microservices Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                          │
│              (Spring Cloud Gateway / 8080)                  │
└────────────────────┬────────────────────────────────────────┘
                     │
        ┌────────────┼────────────┬────────────┐
        │            │            │            │
    ┌───▼───┐   ┌───▼───┐   ┌───▼──────┐   ┌──▼──────┐
    │ Auth  │   │Ticket │   │  Notify  │   │Analytics│
    │Svc    │   │Svc    │   │  Svc     │   │  Svc    │
    └───┬───┘   └───┬───┘   └───┬──────┘   └──┬──────┘
        │           │           │             │
        └───────────┼───────────┼─────────────┘
                    │
            ┌───────▼──────────┐
            │   Eureka Server  │
            │  (Service Disc)  │
            └────────────────┬─┘
                             │
            ┌────────────────┼─────────────┐
            │                │             │
        ┌───▼────┐   ┌──────▼────┐   ┌───▼────┐
        │ Kafka  │   │PostgreSQL │   │ Config │
        │(Events)│   │ (Persist) │   │Server  │
        └────────┘   └───────────┘   └────────┘
```

### Technology Stack

**Core Framework**
- Spring Boot 2.7.14
- Spring Cloud (Eureka, Config Server, Gateway)
- Spring Security with JWT

**Data & Messaging**
- PostgreSQL (Multi-database per service)
- Apache Kafka (Event streaming)
- Spring Data JPA

**Infrastructure**
- Docker & Docker Compose
- Eureka Service Discovery
- Spring Cloud Config Server

**Development Tools**
- Java 17
- Maven
- Lombok
- ModelMapper

## 📦 Services

### 1. **API Gateway** (Port 8080)
Entry point for all client requests with intelligent routing
- Request routing to microservices
- JWT token validation
- Cross-cutting concerns (logging, rate limiting)

### 2. **Auth Service** (Port 8081)
User authentication and authorization
- User registration (CUSTOMER role)
- JWT-based login
- Profile management
- Token refresh

### 3. **Ticket Service** (Port 8082) - Core Support Platform
Support ticket lifecycle management with AI-inspired prioritization
- Create/Read/Update/Delete tickets
- Automatic priority determination (AI engine)
- Ticket assignment to agents
- Status tracking (OPEN → IN_PROGRESS → RESOLVED → CLOSED)
- Category management (technical, billing, general)
- Attachment support

### 4. **Notification Service** (Port 8083)
Real-time event-driven notifications
- Ticket assignment notifications
- Status change alerts
- Support team broadcasts
- Kafka event consumer

### 5. **Analytics Service** (Port 8085)
Support operations metrics and reporting
- Ticket volume metrics
- Resolution time analytics
- Agent performance tracking
- Priority distribution reports
- Customer satisfaction metrics

### 6. **User Service** (Port 8084)
User and agent management
- User profile management
- Agent lifecycle
- Role management
- User directory

### 7. **Config Server** (Port 8888)
Centralized configuration management
- Environment-specific configs
- Service property management

### 8. **Eureka Server** (Port 8761)
Service discovery and registration
- Dynamic service registration
- Load balancing
- Health monitoring

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/Rawdyrathaur/OmniSupport-AI.git
cd OmniSupport-AI
```

2. **Start infrastructure services**
```bash
docker-compose up -d
```

This starts:
- PostgreSQL (omnisupport_db)
- Apache Kafka with Zookeeper
- Kafka UI (http://localhost:9090)

3. **Build all services**
```bash
mvn clean install -DskipTests
```

4. **Start services (in order)**
```bash
# Terminal 1: Config Server
cd config-server && mvn spring-boot:run

# Terminal 2: Eureka Server
cd eureka-server && mvn spring-boot:run

# Terminal 3: Auth Service
cd auth-service && mvn spring-boot:run

# Terminal 4: User Service
cd user-service && mvn spring-boot:run

# Terminal 5: Ticket Service
cd ticket-service && mvn spring-boot:run

# Terminal 6: Notification Service
cd notification-service && mvn spring-boot:run

# Terminal 7: Analytics Service
cd analytics-service && mvn spring-boot:run

# Terminal 8: API Gateway
cd gateway && mvn spring-boot:run
```

5. **Verify services are running**
- Eureka Dashboard: http://localhost:8761/
- Kafka UI: http://localhost:9090/

## 🔌 API Documentation

### Authentication

**Register User**
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "role": "CUSTOMER"
}
```

**Login**
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securePassword123"
}

Response: {
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "...",
  "expiresIn": 3600
}
```

### Ticket Management

**Create Ticket**
```http
POST /api/v1/tickets
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "title": "Server connection timeout",
  "description": "Unable to connect to database server",
  "priority": "HIGH",
  "customerId": "customer-uuid",
  "category": "technical"
}
```

**Get All Tickets**
```http
GET /api/v1/tickets?page=0&size=10
Authorization: Bearer <JWT_TOKEN>
```

**Get Ticket by ID**
```http
GET /api/v1/tickets/{ticketId}
Authorization: Bearer <JWT_TOKEN>
```

**Update Ticket**
```http
PUT /api/v1/tickets/{ticketId}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "title": "Updated title",
  "status": "IN_PROGRESS",
  "priority": "CRITICAL"
}
```

**Assign Ticket**
```http
POST /api/v1/tickets/{ticketId}/assign?agentId=agent-uuid
Authorization: Bearer <JWT_TOKEN>
```

**Update Ticket Status**
```http
POST /api/v1/tickets/{ticketId}/status?status=RESOLVED
Authorization: Bearer <JWT_TOKEN>
```

### Analytics

**Get Analytics Summary**
```http
GET /api/v1/analytics/summary
Authorization: Bearer <JWT_TOKEN>

Response: {
  "totalTickets": 1250,
  "openTickets": 180,
  "resolvedTickets": 950,
  "criticalTickets": 15,
  "avgResolutionTimeHours": 24.5,
  "avgResponseTimeMinutes": 45,
  "customerSatisfactionScore": 4.7
}
```

### Notifications

**Send Notification**
```http
POST /api/v1/notifications/send
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "message": "Ticket #123 has been assigned to you",
  "type": "TICKET_ASSIGNED",
  "recipientId": "agent-uuid",
  "ticketId": "ticket-uuid"
}
```

## 🤖 AI-Inspired Ticket Priority Engine

OmniSupport AI includes an intelligent ticket priority classification system that automatically determines ticket urgency based on content analysis:

**Priority Rules:**
- **CRITICAL**: "server down", "security breach", "payment failed", "production down"
- **HIGH**: "bug", "error", "crash", "failure", "not working"
- **MEDIUM**: Standard support issues (default)
- **LOW**: "feature request", "documentation", "enhancement"

**Example:**
```
Input: 
  Title: "Production server is down"
  Description: "The main API server is unavailable for 30 minutes"
  
Output: 
  Priority: CRITICAL
  Confidence: 0.95
  Reason: "Critical severity detected - contains critical infrastructure keywords"
```

This engine demonstrates a custom, differentiating business logic feature that transforms a generic support platform into an enterprise-grade intelligent system.

## 🔐 Security & Authentication

### Role-Based Access Control (RBAC)

| Role     | Permissions |
|----------|------------|
| ADMIN    | All endpoints, system configuration, analytics |
| AGENT    | Ticket management, ticket assignment, analytics |
| CUSTOMER | Own tickets, notifications, profile management |

### JWT Authentication Flow

```
1. User sends credentials to /auth/login
2. Auth Service validates and generates JWT token
3. Token includes user ID, role, and expiration (1 hour)
4. API Gateway validates JWT on every request
5. Request routed to service with user context
6. Token refresh using refresh token
```

## 📊 Database Schema

### Databases (PostgreSQL)
- `omnisupport_auth` - User credentials and authentication
- `omnisupport_ticket` - Support tickets and related data
- `omnisupport_notification` - Notifications and events
- `omnisupport_analytics` - Metrics and analytics data

### Key Entities

**User**
- id, name, email, password, role, createdAt, updatedAt

**Ticket**
- id, title, description, priority, status, assignedAgentId, customerId, category, attachmentIds, createdAt, updatedAt, resolvedAt

**Notification**
- id, message, type, recipientId, ticketId, isRead, createdAt

## 📡 Event Streaming with Kafka

OmniSupport AI uses Kafka for event-driven communication:

**Topics:**
- `omnisupport-ticket-created` - New ticket creation events
- `omnisupport-ticket-updated` - Ticket update events
- `omnisupport-ticket-assigned` - Ticket assignment events
- `omnisupport-notification-send` - Notification events
- `omnisupport-analytics-event` - Analytics tracking events

## 🧪 Testing

Run all tests:
```bash
mvn clean test
```

Run specific service tests:
```bash
cd ticket-service && mvn test
```

## 📈 Performance & Scalability

- **Horizontal Scaling**: Each microservice can be scaled independently
- **Load Balancing**: API Gateway distributes requests
- **Caching**: Spring Cache abstraction for frequently accessed data
- **Async Processing**: Kafka for non-blocking event processing
- **Connection Pooling**: HikariCP for database connections

## 🚀 Deployment

### Docker Deployment

```bash
# Build all services
mvn clean install

# Deploy with Docker Compose
docker-compose up -d
```

### Production Considerations

- Use environment-specific configurations
- Implement API rate limiting
- Enable HTTPS/TLS
- Set up monitoring and logging
- Configure database backups
- Use container orchestration (Kubernetes)

## 📚 Architecture Patterns

- **Microservices Architecture**: Independent, scalable services
- **API Gateway Pattern**: Single entry point for clients
- **Service Discovery**: Automatic service registration with Eureka
- **Event-Driven Architecture**: Asynchronous communication with Kafka
- **Database per Service**: Independent data stores
- **Configuration Management**: Centralized config with Spring Cloud Config

## 👨‍💻 Developer

**Manish Rathaur**
- GitHub: [@Rawdyrathaur](https://github.com/Rawdyrathaur)
- Portfolio-grade enterprise backend engineering project
- Demonstrates expertise in:
  - Spring Boot microservices
  - Cloud-native architecture
  - Distributed systems
  - SaaS backend engineering

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📞 Support

For questions or issues, please open an issue on GitHub or contact the developer.

---

## 👨‍💻 About the Developer

**Developed by Manish Rathaur**  
**GitHub**: [@Rawdyrathaur](https://github.com/Rawdyrathaur)  

This is a portfolio-grade enterprise backend engineering project demonstrating expertise in:
- Spring Boot microservices architecture
- Cloud-native distributed systems
- SaaS backend platform design
- Enterprise-scale system engineering

---

**OmniSupport AI** - Transforming support operations through cloud-native intelligence.

*Built with ❤️ by Manish Rathaur - Cloud-Native Backend Engineer*
