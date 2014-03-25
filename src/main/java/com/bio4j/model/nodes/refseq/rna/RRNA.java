
package com.bio4j.model.nodes.refseq.rna;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public interface RRNA extends RNA<RRNA, RRNA.type> { 

  enum type implements RNAType<RRNA, RRNA.type> {

    rRNA;
    public type value() { return rRNA; }
  }   
}
