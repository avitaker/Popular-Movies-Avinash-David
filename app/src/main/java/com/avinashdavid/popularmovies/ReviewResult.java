package com.avinashdavid.popularmovies;

/**
 * Created by avinashdavid on 5/29/16.
 */
public class ReviewResult {
    private String resultId;
    public String author;
    public String reviewText;
    public String reviewUrl;

    public ReviewResult(String resultId) {
        this.resultId = resultId;
    }

    public String getResultId() {
        return resultId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }
}
