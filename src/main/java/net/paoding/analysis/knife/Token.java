package net.paoding.analysis.knife;

public class Token {
	private final CharSequence charSequence;
	private final int startOffset;
	private final int endOffset;

	public Token(CharSequence charSequence, int startOffset, int endOffset) {
		super();
		this.charSequence = charSequence;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public CharSequence charSequence() {
		return charSequence;
	}

	public int startOffset() {
		return startOffset;
	}

	public int endOffset() {
		return endOffset;
	}
}
