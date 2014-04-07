package hr.fer.zemris.otd.dataPreprocessing;

import java.util.Arrays;

/**
 * Class representing single post. It extends {@link AbstractPost} so it also
 * has a reference to labels for that post.
 * 
 * @author Sven Vidak
 *
 */
public class Post extends AbstractPost {

	private String postText;

	public Post(int numberOfLabels) {
		super(numberOfLabels);
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getPostText() {
		return postText;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(labels) + "\n");
		sb.append(postText);
		return sb.toString();
	}
}
