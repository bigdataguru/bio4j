package com.bio4j.model;

/*
  A Relationship type. Implementing **concrete** classes should be singleton `Enum`s.

  @author Eduardo Pareja-Tobes <eparejatobes@ohnosequences.com>
*/
public interface RelationshipType <
  S extends Node<S,ST>,
  ST extends Enum<ST> & NodeType<S,ST>,
  R extends Relationship<S,ST,R,RT,T,TT>, 
  RT extends Enum<RT> & RelationshipType<S,ST,R,RT,T,TT>,
  T extends Node<T,TT>,
  TT extends Enum<TT> & NodeType<T,TT>
> {

  /*
    this is a strong hint for you to implement this as a singleton
  */
  public RT value();

  // public ST getSourceType();
  // public TT getTargetType();
}