# OmniSupport AI - System Architecture Document

## Overview

OmniSupport AI is built on a cloud-native microservices architecture designed for scalability, resilience, and independent service deployment. This document provides detailed architectural information.

## Design Principles

1. **Microservices Architecture** - Each service owns its domain and database
2. **Domain-Driven Design** - Services aligned with business domains (tickets, notifications, analytics)
3. **Event-Driven Communication** - Asynchronous messaging via Kafka
4. **Resilience Patterns** - Service discovery, circuit breakers, fallbacks
5. **API-First Design** - REST APIs for service-to-service communication
6. **Single Responsibility** - Each service has a specific, well-defined purpose

## Service Layer Architecture

### Auth Service
**Purpose**: User authentication and authorization
**Technologies**: Spring Security, JWT
**Database**: omnisupport_auth (PostgreSQL)
**Responsibilities**:
- User registration (self-service for CUSTOMER role)
- JWT token generation and validation
- Token refresh mechanism
- Password encryption (BCrypt)
- Role-based access control

**Key Endpoints**:
- POST /api/v1/auth/register
- POST /api/v1/auth/login
- GET /api/v1/auth/profile
- POST /api/v1/auth/refresh-token

### Ticket Service
**Purpose**: Core support ticket lifecycle management
**Technologies**: Spring Data JPA, Kafka Producer
**Database**: omnisupport_ticket (PostgreSQL)
**Responsibilities**:
- Ticket CRUD operations
- Intelligent priority determination (AI engine)
- Ticket assignment to agents
- Status lifecycle management
- Category tracking
- Attachment management
- Kafka event publishing

**Event Producer**:
- omnisupport-ticket-created
- omnisupport-ticket-updated
- omnisupport-ticket-assigned

**Key Endpoints**:
- POST /api/v1/tickets
- GET /api/v1/tickets
- GET /api/v1/tickets/{id}
- PUT /api/v1/tickets/{id}
- POST /api/v1/tickets/{id}/assign
- POST /api/v1/tickets/{id}/status
- DELETE /api/v1/tickets/{id}

**Custom Feature - Priority Engine**:
```
Automatic Priority Classification Algorithm:
1. Extract keywords from title and description
2. Match against priority rules:
   - CRITICAL: infrastructure/security/payment keywords
   - HIGH: error/bug/failure keywords
   - MEDIUM: default
   - LOW: feature/documentation keywords
3. Return priority with confidence score
4. Generate explanation reason
```

### User Service
**Purpose**: User and agent profile management
**Technologies**: Spring Data JPA
**Database**: omnisupport_auth (shared with Auth Service)
**Responsibilities**:
- User profile CRUD
- Agent management
- User directory
- Role management
- User deactivation/activation

**Key Endpoints**:
- GET /api/v1/users
- GET /api/v1/users/{id}
- PUT /api/v1/users/{id}
- POST /api/v1/users/{id}/deactivate
- GET /api/v1/agents

### Notification Service
**Purpose**: Real-time event-driven notifications
**Technologies**: Spring Kafka, WebSocket (future)
**Database**: omnisupport_notification (PostgreSQL)
**Responsibilities**:
- Kafka event consumer
- Notification record creation
- Notification delivery (email/SMS - extensible)
- Notification status tracking
- Read/unread management

**Event Consumer**:
- Listens to: omnisupport-ticket-*
- Publishes to clients via WebSocket (future enhancement)

**Key Endpoints**:
- GET /api/v1/notifications
- GET /api/v1/notifications/{id}
- PUT /api/v1/notifications/{id}/mark-read
- POST /api/v1/notifications/send

### Analytics Service
**Purpose**: Support operations metrics and reporting
**Technologies**: Spring Data JPA, scheduled tasks
**Database**: omnisupport_analytics (PostgreSQL)
**Responsibilities**:
- Metrics aggregation
- Performance analytics
- Agent productivity tracking
- Customer satisfaction metrics
- Trend analysis
- Report generation

**Key Endpoints**:
- GET /api/v1/analytics/summary
- GET /api/v1/analytics/dashboard
- GET /api/v1/analytics/tickets/stats
- GET /api/v1/analytics/agents/performance
- GET /api/v1/analytics/resolution-time
- GET /api/v1/analytics/priority-distribution

### API Gateway
**Purpose**: Single entry point for all client requests
**Technologies**: Spring Cloud Gateway
**Responsibilities**:
- Request routing to microservices
- JWT token validation
- Cross-cutting concerns (logging, tracing)
- Rate limiting (future)
- Request/response transformation

**Routing Rules**:
```
/api/v1/auth/* -> auth-service:8081
/api/v1/tickets/* -> ticket-service:8082
/api/v1/notifications/* -> notification-service:8083
/api/v1/users/* -> user-service:8084
/api/v1/analytics/* -> analytics-service:8085
```

### Eureka Server
**Purpose**: Service discovery and registration
**Responsibilities**:
- Service registration on startup
- Health monitoring
- Service instance lookup
- Load balancing support

### Config Server
**Purpose**: Centralized configuration management
**Responsibilities**:
- Environment-specific configurations
- Property management
- Configuration versioning

## Communication Patterns

### Synchronous Communication (REST)
- Client → API Gateway → Services
- Services → Services (via Feign Client)

### Asynchronous Communication (Kafka)
```
Ticket Service (Producer)
    ↓
Kafka Topic: omnisupport-ticket-*
    ↓
Notification Service (Consumer)
Analytics Service (Consumer)
```

## Data Flow Diagrams

### Ticket Creation Flow
```
1. Client sends POST /api/v1/tickets with JWT
2. API Gateway validates JWT
3. Ticket Service creates ticket in database
4. Priority Engine analyzes content → determines priority
5. Ticket Service publishes omnisupport-ticket-created event
6. Notification Service consumes event → creates notification
7. Response sent to client with ticket details
```

### Authentication Flow
```
1. Client sends credentials to /api/v1/auth/login
2. Auth Service validates credentials
3. Auth Service generates JWT token (exp: 1 hour)
4. JWT contains: userId, role, issued-at, expires-at
5. Token returned to client
6. Client includes token in Authorization header
7. API Gateway validates token signature and expiration
8. Request routed with user context
9. On expiration, client uses refresh token to get new JWT
```

## Database Design

### Entity Relationships

```
User (omnisupport_auth)
  ├── id (PK)
  ├── email (UNIQUE)
  ├── role (ENUM: ADMIN, AGENT, CUSTOMER)
  └── ...

Ticket (omnisupport_ticket)
  ├── id (PK)
  ├── customerId (FK → User.id)
  ├── assignedAgentId (FK → User.id)
  ├── priority (ENUM: LOW, MEDIUM, HIGH, CRITICAL)
  ├── status (ENUM: OPEN, IN_PROGRESS, RESOLVED, CLOSED)
  └── ...

Notification (omnisupport_notification)
  ├── id (PK)
  ├── recipientId (FK → User.id)
  ├── ticketId (FK → Ticket.id)
  └── ...
```

## Security Architecture

### Authentication
- JWT tokens with RS256 signature
- Token expiration: 1 hour
- Refresh token mechanism for extension
- Secure password hashing with BCrypt

### Authorization
- Role-Based Access Control (RBAC)
- Annotation-based security: @PreAuthorize
- Service-level authorization checks

### Transport Security
- HTTPS/TLS for all communication (production)
- Secure headers (X-Frame-Options, X-Content-Type-Options)
- CORS configuration per service

## Scalability Patterns

### Horizontal Scaling
- Each service can run on multiple instances
- API Gateway distributes requests
- Eureka maintains instance registry

### Vertical Scaling
- Connection pool tuning
- Database query optimization
- Cache layer (Redis - future)

### Asynchronous Processing
- Kafka for non-blocking event processing
- Scheduled tasks for batch operations
- Future: message queue for heavy operations

## Monitoring & Observability

### Logging
- Centralized logging (ELK Stack - future)
- Service logs with correlation IDs
- Audit logs for security events

### Metrics
- Spring Boot Actuator endpoints
- Prometheus metrics (future)
- Custom business metrics

### Tracing
- Spring Cloud Sleuth (future)
- Distributed trace correlation IDs

## Deployment Architecture

### Development Environment
- Docker Compose for local services
- PostgreSQL container
- Kafka container
- Eureka/Config servers

### Production Environment (Recommended)
- Kubernetes orchestration
- Managed PostgreSQL (RDS/CloudSQL)
- Managed Kafka (Confluent/MSK)
- Service mesh (Istio - future)
- Container registry (Docker Hub/ECR)

## Technology Stack Summary

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 2.7.14 |
| Cloud | Spring Cloud 2021.0.8 |
| Web | Spring MVC + Spring Cloud Gateway |
| Security | Spring Security + JWT |
| Data | Spring Data JPA + PostgreSQL |
| Messaging | Apache Kafka |
| Service Discovery | Netflix Eureka |
| Configuration | Spring Cloud Config |
| Container | Docker |
| Build | Maven |
| JSON Processing | Jackson |
| ORM | Hibernate |
| Validation | Spring Validation |
| Utilities | Lombok, ModelMapper |

## Performance Considerations

1. **Connection Pooling**: HikariCP with optimal pool size
2. **Database Indexing**: Index on frequently queried fields
3. **Caching Strategy**: Session cache + entity caching
4. **Async Operations**: Kafka for non-blocking writes
5. **API Pagination**: Limit result sets with page/size parameters

## Future Enhancements

1. **Cache Layer**: Redis for frequently accessed data
2. **Message Queue**: For heavy batch operations
3. **Monitoring**: Prometheus + Grafana
4. **Distributed Tracing**: Spring Cloud Sleuth + Zipkin
5. **Service Mesh**: Istio for advanced networking
6. **WebSocket**: Real-time notifications
7. **ML Integration**: Advanced ticket categorization
8. **GraphQL**: Alternative query language

---

**Document Version**: 1.0  
**Last Updated**: 2026-05-28  
**Author**: Manish Rathaur
