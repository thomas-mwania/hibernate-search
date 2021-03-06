/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.document.model.dsl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.search.backend.lucene.analysis.model.impl.LuceneAnalysisDefinitionRegistry;
import org.hibernate.search.backend.lucene.document.model.impl.AbstractLuceneIndexSchemaFieldNode;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexModel;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexSchemaValueFieldNode;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexSchemaValueFieldTemplate;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexSchemaNodeCollector;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexSchemaObjectFieldNode;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexSchemaObjectFieldTemplate;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexSchemaObjectNode;
import org.hibernate.search.backend.lucene.document.model.impl.LuceneIndexSchemaRootNode;
import org.hibernate.search.backend.lucene.types.dsl.LuceneIndexFieldTypeFactory;
import org.hibernate.search.backend.lucene.types.dsl.impl.LuceneIndexFieldTypeFactoryImpl;
import org.hibernate.search.engine.backend.document.model.dsl.spi.IndexSchemaBuildContext;
import org.hibernate.search.engine.backend.types.converter.spi.ToDocumentIdentifierValueConverter;
import org.hibernate.search.engine.backend.types.converter.spi.StringToDocumentIdentifierValueConverter;
import org.hibernate.search.engine.backend.document.model.dsl.spi.IndexSchemaRootNodeBuilder;
import org.hibernate.search.engine.mapper.mapping.building.spi.IndexFieldTypeDefaultsProvider;
import org.hibernate.search.engine.reporting.spi.EventContexts;
import org.hibernate.search.util.common.reporting.EventContext;

public class LuceneIndexSchemaRootNodeBuilder extends AbstractLuceneIndexSchemaObjectNodeBuilder
		implements IndexSchemaRootNodeBuilder, IndexSchemaBuildContext {

	private final EventContext indexEventContext;
	private final String mappedTypeName;
	private final LuceneAnalysisDefinitionRegistry analysisDefinitionRegistry;

	private ToDocumentIdentifierValueConverter<?> idDslConverter;

	public LuceneIndexSchemaRootNodeBuilder(EventContext indexEventContext,
			String mappedTypeName, LuceneAnalysisDefinitionRegistry analysisDefinitionRegistry) {
		this.indexEventContext = indexEventContext;
		this.mappedTypeName = mappedTypeName;
		this.analysisDefinitionRegistry = analysisDefinitionRegistry;
	}

	@Override
	public EventContext eventContext() {
		return getIndexEventContext().append( EventContexts.indexSchemaRoot() );
	}

	@Override
	public LuceneIndexFieldTypeFactory createTypeFactory(IndexFieldTypeDefaultsProvider defaultsProvider) {
		return new LuceneIndexFieldTypeFactoryImpl( indexEventContext, analysisDefinitionRegistry, defaultsProvider );
	}

	@Override
	public void explicitRouting() {
		// Nothing to do
	}

	@Override
	public void idDslConverter(ToDocumentIdentifierValueConverter<?> idDslConverter) {
		this.idDslConverter = idDslConverter;
	}

	@Override
	public LuceneIndexSchemaRootNodeBuilder getRootNodeBuilder() {
		return this;
	}

	public LuceneIndexModel build(String indexName) {
		Map<String, LuceneIndexSchemaObjectFieldNode> objectFieldNodes = new HashMap<>();
		Map<String, LuceneIndexSchemaValueFieldNode<?>> fieldNodes = new HashMap<>();
		List<LuceneIndexSchemaObjectFieldTemplate> objectFieldTemplates = new ArrayList<>();
		List<LuceneIndexSchemaValueFieldTemplate> fieldTemplates = new ArrayList<>();

		LuceneIndexSchemaNodeCollector collector = new LuceneIndexSchemaNodeCollector() {
			@Override
			public void collect(String absoluteFieldPath, LuceneIndexSchemaValueFieldNode<?> node) {
				fieldNodes.put( absoluteFieldPath, node );
			}

			@Override
			public void collect(String absolutePath, LuceneIndexSchemaObjectFieldNode node) {
				objectFieldNodes.put( absolutePath, node );
			}

			@Override
			public void collect(LuceneIndexSchemaObjectFieldTemplate template) {
				objectFieldTemplates.add( template );
			}

			@Override
			public void collect(LuceneIndexSchemaValueFieldTemplate template) {
				fieldTemplates.add( template );
			}
		};

		List<AbstractLuceneIndexSchemaFieldNode> staticChildren = new ArrayList<>();
		LuceneIndexSchemaObjectNode rootNode = new LuceneIndexSchemaRootNode( staticChildren );
		contributeChildren( rootNode, collector, staticChildren );

		return new LuceneIndexModel(
				indexName,
				mappedTypeName,
				idDslConverter == null ? new StringToDocumentIdentifierValueConverter() : idDslConverter,
				rootNode, objectFieldNodes, fieldNodes,
				objectFieldTemplates, fieldTemplates
		);
	}

	@Override
	String getAbsolutePath() {
		return null;
	}

	EventContext getIndexEventContext() {
		return indexEventContext;
	}
}
