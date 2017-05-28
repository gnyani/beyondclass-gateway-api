package api;

import java.time.LocalDateTime;
import java.util.List;

public class Review {
	
	private User userName;
	private LocalDateTime reviewTime;
	private String comment;
	private String commentHead;
	private List<Review> replies;
	private List<User> likes;

	@Override
	public String toString() {
		return "Review{" +
				"userName=" + userName +
				", reviewTime=" + reviewTime +
				", comment='" + comment + '\'' +
				", commentHead='" + commentHead + '\'' +
				", replies=" + replies +
				", likes=" + likes +
				'}';
	}
	public User getUserName() {
		return userName;
	}
	public void setUserName(User userName) {
		this.userName = userName;
	}
	public LocalDateTime getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(LocalDateTime reviewTime) {
		this.reviewTime = reviewTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentHead() {
		return commentHead;
	}
	public void setCommentHead(String commentHead) {
		this.commentHead = commentHead;
	}
	public List<Review> getReplies() {
		return replies;
	}
	public void setReplies(List<Review> replies) {
		this.replies = replies;
	}
	public List<User> getLikes() {
		return likes;
	}
	public void setLikes(List<User> likes) {
		this.likes = likes;
	}

	
}
