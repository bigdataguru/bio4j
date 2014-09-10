package com.bio4j.model.uniprot.relationships;

import com.bio4j.model.uniprot.UniprotGraph;
import com.bio4j.model.uniprot.nodes.Consortium;
import com.bio4j.model.uniprot.nodes.Person;
import com.bio4j.model.uniprot.nodes.Reference;
import com.ohnosequences.typedGraphs.UntypedGraph;

/**
 * Created by ppareja on 9/9/2014.
 */
public final class ReferenceAuthorConsortium <I extends UntypedGraph<RV, RVT, RE, RET>, RV, RVT, RE, RET>
		extends
		UniprotGraph.UniprotEdge<
				Reference<I, RV, RVT, RE, RET>, UniprotGraph<I, RV, RVT, RE, RET>.ReferenceType,
				ReferenceAuthorConsortium<I, RV, RVT, RE, RET>, UniprotGraph<I, RV, RVT, RE, RET>.ReferenceAuthorConsortiumType,
				Consortium<I, RV, RVT, RE, RET>, UniprotGraph<I, RV, RVT, RE, RET>.ConsortiumType,
				I, RV, RVT, RE, RET
				> {

	public ReferenceAuthorConsortium(RE edge, UniprotGraph<I, RV, RVT, RE, RET>.ReferenceAuthorConsortiumType type) {

		super(edge, type);
	}

	@Override
	public ReferenceAuthorConsortium<I, RV, RVT, RE, RET> self() {
		return this;
	}
}