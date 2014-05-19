package com.bio4j.model.properties;

import com.ohnosequences.typedGraphs.Property;
import com.ohnosequences.typedGraphs.PropertyType;

import com.ohnosequences.typedGraphs.Element;
import com.ohnosequences.typedGraphs.ElementType;

public interface Definition <N extends Element<N,NT>, NT extends ElementType<N,NT>> 
  extends Property<N, NT> {

  // the property method
  public String definition();

  // static type method
  public static <
    N extends Element<N,NT> & Definition<N,NT>, 
    NT extends ElementType<N,NT>
  > Type<N,NT> TYPE(NT elementType) { return new Type<N,NT>(elementType); }

  // convenience type
  public class Type <N extends Element<N,NT> & Definition<N,NT>, NT extends ElementType<N,NT>> 
    extends PropertyType<N, NT, Definition<N,NT>, Type<N,NT>, String> {

    public Type(NT elementType) {
      super(elementType, "definition");
    }

    public Class<String> valueClass() { return String.class; }
  }
}