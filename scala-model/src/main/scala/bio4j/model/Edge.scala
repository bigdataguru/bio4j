package bio4j.model

trait AnyEdge extends Denotation[AnyEdgeType] { self =>

  // NOTE: It doesn't make any sense, but if I remove this from here type inference fails in tests
  type Tpe <: AnyEdgeType
  val tpe: Tpe

  /* Source-Edge-Target types */
  type SourceType = tpe.SourceType
  val sourceType: SourceType = tpe.sourceType

  type TargetType = tpe.TargetType
  val targetType: TargetType = tpe.targetType

  /* Get source/target from this representation */
  abstract case class GetSource[V <: AnyVertex.ofType[SourceType]](val v: V) {
    def apply(rep: TaggedRep): v.TaggedRep
  }
  abstract case class GetTarget[V <: AnyVertex.ofType[TargetType]](val v: V) {
    def apply(rep: TaggedRep): v.TaggedRep
  }

  implicit def edgeOps(rep: self.TaggedRep) = EdgeOps(rep)
  case class   EdgeOps(rep: self.TaggedRep) {

    def source[V <: AnyVertex.ofType[SourceType]](implicit getter: GetSource[V]) = getter(rep)

    def target[V <: AnyVertex.ofType[TargetType]](implicit getter: GetTarget[V]) = getter(rep)

  }

}

class Edge[ET <: AnyEdgeType](val tpe: ET) 
  extends AnyEdge { type Tpe = ET }

object AnyEdge {
  type withSourceType[VT <: AnyVertexType] = AnyEdge { type SourceType = VT }
  type withTargetType[VT <: AnyVertexType] = AnyEdge { type TargetType = VT }
}