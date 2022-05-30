package t3;

import java.util.Objects;

/**
 * long値を参照渡しするためのクラス。
 */
public class LongReference {
	/**
	 * long値。
	 */
	public long val;
	
	public LongReference() {
		this.val = 0;
	}

	public LongReference(long val) {
		this.val = val;
	}

	/**
	 * Generate hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(val);
	}

	/**
	 * Generate equals()
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LongReference other = (LongReference) obj;
		return val == other.val;
	}

	/**
	 * Generate toString()
	 */
	@Override
	public String toString() {
		return "LongReference [val=" + val + "]";
	}

}
