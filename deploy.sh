#!/bin/bash

###############################################################################
# Spring Boot Backend Deployment Script for AWS Elastic Beanstalk
#
# Usage: ./deploy.sh [environment]
#   environment: Environment name (default: resume-chatbot-env-v5)
#
# Example: ./deploy.sh resume-chatbot-env-v5
###############################################################################

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
PROJECT_DIR="/Users/xiemingda/Downloads/doitou"
ENV_NAME="${1:-resume-chatbot-env-v5}"  # Default to v5
REGION="us-east-1"
APP_NAME="resume-chatbot-backend"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  AWS Backend Deployment Script${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Environment: $ENV_NAME"
echo "Region: $REGION"
echo ""

# Step 1: Build the JAR
echo -e "${YELLOW}Step 1/5: Building JAR file...${NC}"
cd $PROJECT_DIR
./mvnw clean package -DskipTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ JAR built successfully${NC}"
else
    echo -e "${RED}✗ Build failed!${NC}"
    exit 1
fi

# Step 2: Copy JAR to root
echo -e "${YELLOW}Step 2/5: Preparing deployment package...${NC}"
cp target/doitou-0.0.1-SNAPSHOT.jar ./application.jar
echo -e "${GREEN}✓ JAR copied to application.jar${NC}"

# Step 3: Create deployment ZIP
echo -e "${YELLOW}Step 3/5: Creating deployment package...${NC}"
TIMESTAMP=$(date +%y%m%d_%H%M%S)
ZIP_NAME="deploy-${TIMESTAMP}.zip"
zip -r $ZIP_NAME application.jar Procfile .ebextensions/ -q
echo -e "${GREEN}✓ Created $ZIP_NAME${NC}"

# Step 4: Upload to S3
echo -e "${YELLOW}Step 4/5: Uploading to S3...${NC}"
S3_BUCKET="elasticbeanstalk-${REGION}-152247411320"
S3_KEY="${APP_NAME}/${ZIP_NAME}"
aws s3 cp $ZIP_NAME s3://${S3_BUCKET}/${S3_KEY} --quiet
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Uploaded to S3${NC}"
else
    echo -e "${RED}✗ S3 upload failed!${NC}"
    exit 1
fi

# Step 5: Deploy to Elastic Beanstalk
echo -e "${YELLOW}Step 5/5: Deploying to Elastic Beanstalk...${NC}"

# Create application version
VERSION_LABEL="v-${TIMESTAMP}"
aws elasticbeanstalk create-application-version \
    --application-name $APP_NAME \
    --version-label $VERSION_LABEL \
    --source-bundle S3Bucket="${S3_BUCKET}",S3Key="${S3_KEY}" \
    --region $REGION \
    --output text > /dev/null

# Deploy to environment
aws elasticbeanstalk update-environment \
    --environment-name $ENV_NAME \
    --version-label $VERSION_LABEL \
    --region $REGION \
    --query 'Status' \
    --output text

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✓ Deployment Initiated!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Version: $VERSION_LABEL"
echo "Environment: $ENV_NAME"
echo ""
echo "Monitor deployment:"
echo "  aws elasticbeanstalk describe-environments --environment-names $ENV_NAME --region $REGION"
echo ""
echo "Or check status:"
echo "  cd $PROJECT_DIR && eb status $ENV_NAME"
echo ""
echo "View logs:"
echo "  cd $PROJECT_DIR && eb logs $ENV_NAME"
echo ""
echo "Test endpoint (wait ~2 minutes):"
echo "  curl http://${ENV_NAME}.eba-cqqaf2qn.us-east-1.elasticbeanstalk.com/api/test/ping"
echo ""

# Cleanup
rm -f $ZIP_NAME application.jar

echo -e "${GREEN}Deployment package cleaned up${NC}"
echo ""
