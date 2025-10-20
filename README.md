---
title: career_conversation
app_file: app.py
sdk: gradio
sdk_version: 5.34.2
---

# AI Career Chatbot - Full-Stack System

> **Live Demo**: [https://huggingface.co/spaces/Doitou/career_conversation](https://huggingface.co/spaces/Doitou/career_conversation)

A production full-stack AI chatbot combining Python Gradio frontend with Java Spring Boot backend, deployed on Hugging Face Spaces and AWS infrastructure. Features session tracking, conversation persistence, and contact management with MySQL database.

## Architecture & Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                         USER                                     │
│              Opens chatbot in browser                            │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│               GRADIO FRONTEND (Hugging Face Spaces)              │
│                                                                   │
│  • Python 3.12 + Gradio UI                                       │
│  • Generates unique session ID (UUID)                            │
│  • Sends user message to OpenAI                                  │
└────────────┬────────────────────────────────────┬───────────────┘
             │                                    │
             │ REST API                           │
             │ POST /api/chat/save                │ OpenAI API
             │ POST /api/contacts/save            │
             ▼                                    ▼
┌──────────────────────────────────┐    ┌────────────────────┐
│  SPRING BOOT BACKEND             │    │   OpenAI API       │
│  (AWS Elastic Beanstalk)         │    │   GPT-4o-mini      │
│                                  │    │                    │
│  • Receives API calls            │    │  • Processes prompt│
│  • Validates data                │    │  • Returns response│
│  • Saves to database             │    │  • Function calls  │
│  • Returns confirmation          │    └────────────────────┘
└────────────┬─────────────────────┘
             │
             │ JDBC
             ▼
┌──────────────────────────────────┐
│      MYSQL DATABASE              │
│      (AWS RDS)                   │
│                                  │
│  Tables:                         │
│  • conversations - chat logs     │
│  • sessions - user tracking      │
│  • user_contacts - lead capture  │
│  • unknown_questions - gaps      │
└──────────────────────────────────┘
```

## Data Flow Example

**User asks a question:**

1. **User** → Types message in Gradio chat interface
2. **Frontend** → Generates session ID from `request.session_hash`
3. **Frontend** → Calls backend API to save user message
   ```
   POST /api/chat/save
   {"userId": "abc-123", "role": "user", "message": "What's your experience?"}
   ```
4. **Backend** → Saves to `conversations` table, updates `sessions` table
5. **Frontend** → Sends message + context to OpenAI API
6. **OpenAI** → Returns AI response (may include function calls)
7. **Frontend** → If AI can't answer, triggers `record_unknown_question()` tool
8. **Frontend** → Calls backend API to save assistant message
   ```
   POST /api/chat/save
   {"userId": "abc-123", "role": "assistant", "message": "I have 5 years..."}
   ```
9. **Frontend** → Displays response to user

**User provides contact info:**

1. **User** → Mentions email in conversation
2. **OpenAI** → Detects email via function calling, triggers `record_user_details()` tool
3. **Frontend** → Calls backend API
   ```
   POST /api/contacts/save
   {"email": "user@example.com", "name": "John", "notes": "Interested in role"}
   ```
4. **Backend** → Saves to `user_contacts` table (unique email constraint)
5. **Frontend** → Sends Pushover notification (optional)
6. **Frontend** → Continues conversation

## Technology Stack

| Component | Technology |
|-----------|------------|
| **Frontend** | Python 3.12, Gradio 5.9, OpenAI SDK |
| **Backend** | Java 17, Spring Boot 3.5.6, Spring Data JPA |
| **Database** | MySQL 8.0 (AWS RDS) |
| **AI** | OpenAI GPT-4o-mini |
| **Analytics** | Chart.js, Plotly, Gradio (dual dashboard options) |
| **CI/CD** | GitHub Actions, AWS CLI |
| **Hosting** | Hugging Face Spaces (frontend), AWS Elastic Beanstalk (backend) |
| **Infrastructure** | AWS EC2, RDS, S3, Application Load Balancer |

## Key Features

- **Session Tracking**: Each browser session gets unique UUID for analytics
- **Conversation Persistence**: All messages saved to MySQL database
- **Contact Management**: Captures user emails and details automatically
- **Unknown Question Logging**: Tracks what AI can't answer for improvement
- **Analytics Dashboard**: Real-time metrics visualization with Chart.js (DAU, conversion rate, top questions)
- **CI/CD Pipeline**: Automated deployment via GitHub Actions to AWS Elastic Beanstalk
- **Hybrid Architecture**: Python for AI/UI, Java for data persistence
- **Production Ready**: Deployed on AWS with auto-healing and backups

## API Endpoints

### Core Endpoints
```bash
# Health check
GET /api/test/ping

# Save conversation
POST /api/chat/save
Body: {"userId": "session-id", "role": "user|assistant", "message": "text"}

# Get chat history
GET /api/chat/history/{userId}

# Save contact
POST /api/contacts/save
Body: {"email": "user@example.com", "name": "Name", "notes": "Notes"}

# Log unknown question
POST /api/questions/unknown
Body: {"question": "Question text"}
```

### Analytics Endpoints
```bash
# Overview metrics (total users, conversations, conversion rate)
GET /api/analytics/overview

# Daily active users (configurable time range)
GET /api/analytics/daily-active-users?days=30

# Top N most frequently asked questions
GET /api/analytics/top-questions?limit=10

# Email capture conversion rate
GET /api/analytics/conversion-rate

# Engagement metrics for specific period
GET /api/analytics/engagement?days=7
```

**Analytics Dashboard**: Access at `http://localhost:8080/analytics.html` (or `/analytics_dashboard.py` for Gradio version)

## Quick Setup

**Frontend (Local)**:
```bash
cd 1_foundations
pip install -r requirements.txt
export OPENAI_API_KEY=your_key
export BACKEND_URL=http://localhost:8080
python app.py
```

**Backend (Local)**:
```bash
cd doitou
./mvnw spring-boot:run
```

**Deploy to Production**:
```bash
# Backend to AWS (manual)
./deploy.sh

# Or use CI/CD (automatic on push to main)
git push origin main

# Frontend to Hugging Face
python deploy_to_hf.py
```

## CI/CD Pipeline

Automated deployment via GitHub Actions on every push to `main` branch:

**Workflow Steps:**
1. Build Maven project with Java 17
2. Package Spring Boot JAR
3. Upload to AWS S3
4. Create Elastic Beanstalk version
5. Deploy to production environment
6. Health check validation

**Configuration**: `.github/workflows/deploy.yml`

**Required Secrets** (set in GitHub repo settings):
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

**Manual Trigger**: Use "Actions" tab in GitHub or `workflow_dispatch` event

---

**Live Application**: [https://huggingface.co/spaces/Doitou/career_conversation](https://huggingface.co/spaces/Doitou/career_conversation)

**Backend API**: [http://resume-chatbot-env-v5.eba-cqqaf2qn.us-east-1.elasticbeanstalk.com/api/test/ping](http://resume-chatbot-env-v5.eba-cqqaf2qn.us-east-1.elasticbeanstalk.com/api/test/ping)

**Analytics Dashboard**: Available in production at `/analytics.html` endpoint
