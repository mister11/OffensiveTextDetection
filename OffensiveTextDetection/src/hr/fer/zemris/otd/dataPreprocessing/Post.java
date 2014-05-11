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

	/**
	 * 
	 */
	private static final long serialVersionUID = 3603487520803691275L;
	private String postText;
	private String mark; // used in statistical data -> examples and corner
							// cases

	public Post(int numberOfLabels) {
		super(numberOfLabels);
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getPostText() {
		return postText;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMark() {
		return mark;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(labels) + System.lineSeparator());
		sb.append(postText);
		return sb.toString();
	}
}
