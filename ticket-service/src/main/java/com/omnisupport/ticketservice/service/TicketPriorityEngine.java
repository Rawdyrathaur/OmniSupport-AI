package com.omnisupport.ticketservice.service;

import com.omnisupport.ticketservice.enums.Priority;
import org.springframework.stereotype.Component;

/**
 * AI-Inspired Ticket Priority Engine
 * Automatically determines ticket priority based on content analysis
 * 
 * Implements intelligent rules for categorizing support tickets by urgency
 * This is a differentiating feature that demonstrates custom SaaS business logic
 */
@Component
public class TicketPriorityEngine {

    /**
     * Analyze ticket content and determine priority automatically
     * 
     * Rules:
     * - CRITICAL: Contains "server down", "security breach", "payment failed", "critical", "urgent", "production down"
     * - HIGH: Contains "important", "high priority", "immediate", "bug", "error", "failure", "not working"
     * - MEDIUM: Default for standard issues
     * - LOW: Contains "documentation", "feature request", "enhancement", "question"
     * 
     * @param title Ticket title
     * @param description Ticket description
     * @return Automatically determined Priority
     */
    public Priority analyzePriority(String title, String description) {
        String combinedText = (title + " " + description).toLowerCase();
        
        // Check for CRITICAL keywords
        if (containsAny(combinedText, 
            "server down", "outage", "production down", "service unavailable",
            "security breach", "security incident", "data breach", "hacked",
            "payment failed", "payment error", "billing issue - critical",
            "critical", "urgent", "sev-1", "p1", "blocker")) {
            return Priority.CRITICAL;
        }
        
        // Check for HIGH priority keywords
        if (containsAny(combinedText,
            "important", "high priority", "immediate", "asap",
            "bug", "error", "exception", "crash", "failure",
            "not working", "broken", "down", "issue", "problem",
            "high", "sev-2", "p2")) {
            return Priority.HIGH;
        }
        
        // Check for LOW priority keywords
        if (containsAny(combinedText,
            "documentation", "docs", "guide", "help",
            "feature request", "enhancement", "improvement",
            "question", "how to", "tutorial", "question", "inquiry")) {
            return Priority.LOW;
        }
        
        // Default to MEDIUM
        return Priority.MEDIUM;
    }

    /**
     * Determine if text contains any of the given keywords
     * @param text Text to search in
     * @param keywords Keywords to search for
     * @return true if any keyword is found
     */
    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get priority recommendation with confidence score
     * @param title Ticket title
     * @param description Ticket description
     * @return PriorityRecommendation with priority and confidence
     */
    public PriorityRecommendation analyzePriorityWithConfidence(String title, String description) {
        Priority priority = analyzePriority(title, description);
        String combinedText = (title + " " + description).toLowerCase();
        
        double confidence = calculateConfidence(combinedText, priority);
        
        return PriorityRecommendation.builder()
            .priority(priority)
            .confidence(confidence)
            .reason(generateReason(priority, combinedText))
            .build();
    }

    /**
     * Calculate confidence score for priority determination
     * @param text Combined ticket text
     * @param priority Determined priority
     * @return Confidence score (0.0 to 1.0)
     */
    private double calculateConfidence(String text, Priority priority) {
        int matchCount = 0;
        int totalKeywords = 0;
        
        String[] keywords = getKeywordsForPriority(priority);
        totalKeywords = keywords.length;
        
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                matchCount++;
            }
        }
        
        return totalKeywords > 0 ? (double) matchCount / totalKeywords : 0.5;
    }

    /**
     * Get keywords associated with a priority level
     */
    private String[] getKeywordsForPriority(Priority priority) {
        return switch (priority) {
            case CRITICAL -> new String[]{
                "server down", "outage", "production down", "security breach", 
                "payment failed", "critical", "urgent"
            };
            case HIGH -> new String[]{
                "important", "high priority", "immediate", "bug", "error", "crash", "failure"
            };
            case LOW -> new String[]{
                "documentation", "feature request", "enhancement", "question"
            };
            default -> new String[]{"standard", "normal", "regular"};
        };
    }

    /**
     * Generate human-readable reason for priority determination
     */
    private String generateReason(Priority priority, String text) {
        return switch (priority) {
            case CRITICAL -> "Critical severity detected - contains critical infrastructure or security keywords";
            case HIGH -> "High priority detected - contains urgent or bug-related keywords";
            case LOW -> "Low priority detected - contains feature request or documentation keywords";
            default -> "Standard priority assigned - no specific indicators detected";
        };
    }

    /**
     * DTO for priority recommendation response
     */
    public static class PriorityRecommendation {
        public Priority priority;
        public double confidence;
        public String reason;

        public PriorityRecommendation() {}

        public PriorityRecommendation(Priority priority, double confidence, String reason) {
            this.priority = priority;
            this.confidence = confidence;
            this.reason = reason;
        }

        public static PriorityRecommendationBuilder builder() {
            return new PriorityRecommendationBuilder();
        }

        public static class PriorityRecommendationBuilder {
            private Priority priority;
            private double confidence;
            private String reason;

            public PriorityRecommendationBuilder priority(Priority priority) {
                this.priority = priority;
                return this;
            }

            public PriorityRecommendationBuilder confidence(double confidence) {
                this.confidence = confidence;
                return this;
            }

            public PriorityRecommendationBuilder reason(String reason) {
                this.reason = reason;
                return this;
            }

            public PriorityRecommendation build() {
                return new PriorityRecommendation(this.priority, this.confidence, this.reason);
            }
        }
    }
}
