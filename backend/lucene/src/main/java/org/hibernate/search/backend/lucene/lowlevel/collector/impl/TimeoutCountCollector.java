/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.lowlevel.collector.impl;

import org.hibernate.search.backend.lucene.search.timeout.impl.LuceneTimeoutManager;

import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;

/**
 * Counts the total hit, as {@link org.apache.lucene.search.TotalHitCountCollector} does.
 * Moreover, it periodically checks for timeout.
 *
 */
public class TimeoutCountCollector extends SimpleCollector {

	private final LuceneTimeoutManager timeoutManager;

	public TimeoutCountCollector(LuceneTimeoutManager timeoutManager) {
		this.timeoutManager = timeoutManager;
	}

	private int totalHits;

	/** Returns how many hits matched the search. */
	public int getTotalHits() {
		return totalHits;
	}

	@Override
	public void collect(int doc) {
		// Check for timeout each 256 elements:
		if ( totalHits % 256 == 0 ) {
			// Fail if hard timeout, ignore if soft timeout
			timeoutManager.checkTimedOut();
		}
		totalHits++;
	}

	@Override
	public ScoreMode scoreMode() {
		return ScoreMode.COMPLETE_NO_SCORES;
	}
}
