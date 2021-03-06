/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.integrationtest.mapper.pojo.work;

import java.util.concurrent.CompletableFuture;

import org.hibernate.search.mapper.javabean.work.SearchIndexer;
import org.hibernate.search.mapper.javabean.work.SearchIndexingPlan;
import org.hibernate.search.util.impl.integrationtest.common.rule.BackendMock;

public class PojoIndexingAddIT extends AbstractPojoIndexingOperationIT {

	@Override
	protected boolean isAdd() {
		return true;
	}

	@Override
	protected void expectOperation(BackendMock.DocumentWorkCallListContext context, String tenantId,
			String id, String routingKey, String value) {
		context.add( b -> addWorkInfoAndDocument( b, tenantId, id, routingKey, value ) );
	}

	@Override
	protected void addTo(SearchIndexingPlan indexingPlan, int id) {
		indexingPlan.add( createEntity( id ) );
	}

	@Override
	protected void addTo(SearchIndexingPlan indexingPlan, Object providedId, int id) {
		indexingPlan.add( providedId, null, createEntity( id ) );
	}

	@Override
	protected void addTo(SearchIndexingPlan indexingPlan, Object providedId, String providedRoutingKey, int id) {
		indexingPlan.add( providedId, providedRoutingKey, createEntity( id ) );
	}

	@Override
	protected CompletableFuture<?> execute(SearchIndexer indexer, int id) {
		return indexer.add( createEntity( id ) );
	}

	@Override
	protected CompletableFuture<?> execute(SearchIndexer indexer, Object providedId, int id) {
		return indexer.add( providedId, createEntity( id ) );
	}

	@Override
	protected CompletableFuture<?> execute(SearchIndexer indexer, Object providedId, String providedRoutingKey, int id) {
		return indexer.add( providedId, providedRoutingKey, createEntity( id ) );
	}
}
