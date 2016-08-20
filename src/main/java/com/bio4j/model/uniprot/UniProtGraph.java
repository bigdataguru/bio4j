package com.bio4j.model.buh;

import com.bio4j.angulillos.*;
import com.bio4j.angulillos.Arity.*;

import java.util.Date;
import java.util.Optional;

public final class UniProtGraph<V,E> extends TypedGraph<UniProtGraph<V,E>,V,E> {

  public UniProtGraph(UntypedGraph<V,E> graph) { super(graph); }

  @Override public final UniProtGraph<V,E> self() { return this; }

  /*
    ## Proteins

    The first thing to point out as clearly as possible is: `Protein`s **do not** correspond to UniProt entries; we create a protein for every isoform present in any UniProt entry.
  */
  public final class Protein extends Vertex<Protein> {

    private Protein(V vertex) { super(vertex, protein); }
    @Override public final Protein self() { return this; }
  }

  public final ProteinType protein = new ProteinType();
  public final class ProteinType extends VertexType<Protein> {

    @Override public final Protein fromRaw(V vertex) { return new Protein(vertex); }

    /*
      #### Accession

      Accessions are the primary IDs for UniProt proteins. See http://www.uniprot.org/help/accession_numbers. Note that this is *never* the *entry* accession, but the isoform id. Those isoforms which are the canonical sequence of an entry have `isCanonical` set to `true`.
    */
    public final Accession accession = new Accession();
    public final class Accession extends Property<String> implements FromAtMostOne, ToOne {
      private Accession() { super(String.class); }
      public final Index index = new Index();
      public final class Index extends UniqueIndex<Accession, String> {
        private Index() { super(accession); }
      }
    }

    /*
      #### Is canonical

      Whether this protein is the canonical protein of an entry.
    */
    public final IsCanonical isCanonical = new IsCanonical();
    public final class IsCanonical extends Property<Boolean> implements FromAtLeastOne, ToOne {
      private IsCanonical() { super(Boolean.class); }
    }

    /*
      #### Full name

      UniProt has a lot of names for each protein, and we try to use here the best one for each. This property has the full recommended name (if present), otherwise the full submitted name, plus possibly the isoform name.
    */
    public final FullName fullName = new FullName();
    public final class FullName extends Property<String> implements FromAny, ToOne {
      private FullName() { super(String.class); }
    }

    /*
      #### Dataset

      Whether this entry comes from Swiss-Prot or TrEMBL.
    */
    public final Dataset dataset = new Dataset();
    public final class Dataset extends Property<Datasets> implements FromAny, ToOne {
      private Dataset() { super(Datasets.class); }
    }

    /*
      #### Gene name

      A name for the (a) gene codifying this protein. Corresponds to the primary name in UniProt data. Note that not all proteins have an associated gene name.
    */
    public final GeneName geneName = new GeneName();
    public final class GeneName extends Property<String> implements FromAny {
      private GeneName() { super(String.class); }
      public final Index index = new Index();
      public final class Index extends NonUniqueIndex<GeneName, String> {
        private Index() { super(geneName); }
      }
    }

    /*
      #### ORF name

      The name, if any, for the (a) ORF corresponding to this protein. ORF name in UniProt data.
    */
    public final ORFName orfName = new ORFName();
    public final class ORFName extends Property<String> implements FromAny {
      private ORFName() { super(String.class); }
    }

    /*
      #### Existence

      How strong is the evidence for this protein existence. This is only available for proteins which are the canonical protein of some entry.
    */
    public final Existence existence = new Existence();
    public final class Existence extends Property<ExistenceEvidence> implements FromAny {
      private Existence() { super(ExistenceEvidence.class); }
    }

    /*
      #### Sequence

      Normalized to all-caps.
    */
    public final Sequence sequence = new Sequence();
    public final class Sequence extends Property<String> implements FromAny, ToOne {
      private Sequence() { super(String.class); }
    }

    /*
      #### Sequence Mass

      I honestly don't know what's this.
    */
    public final Mass mass = new Mass();
    public final class Mass extends Property<Integer> implements FromAny, ToOne {
      private Mass() { super(Integer.class); }
    }
  }

  /*
    See ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/docs/userman.htm#OC_line
  */
  public static enum GeneLocations {

    chromosome, // TODO this the default case, which is missing in UniProt. Please fix/improve this name.
    apicoplast,
    chloroplast,
    organellar_chromatophore,
    cyanelle,
    hydrogenosome,
    mitochondrion,
    non_photosynthetic_plastid,
    nucleomorph,
    plasmid,
    plastid;
  }

  public static enum Datasets {

    swissProt,
    trEMBL;
  }

  public static enum ExistenceEvidence {

    proteinLevel,
    transcriptLevel,
    homologyInferred,
    predicted,
    uncertain;
  }

  /*
    ### Keywords

    See

    1. http://www.uniprot.org/help/keywords
    2. ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/docs/userman.htm#KW_line
    3. ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/docs/keywlist.txt
    4. http://geneontology.org/external2go/uniprotkb_kw2go

    Note that from http://www.uniprot.org/keywords/ you can download a `tsv` file with columns ID, description, category. If we also want the name I'm afraid we'll need to extract it from http://www.uniprot.org/docs/keywlist.
  */
  public final class Keyword extends Vertex<Keyword> {
    private Keyword(V vertex) { super(vertex, keyword); }
    @Override public final Keyword self() { return this; }
  }
  public final KeywordType keyword = new KeywordType();

  public final class KeywordType extends VertexType<Keyword> {

    @Override public final Keyword fromRaw(V vertex) { return new Keyword(vertex); }

    public final Name name = new Name();
    public final class Name extends Property<String> implements FromAny, ToOne {
      private Name() { super(String.class); }
    }

    public final Definition definition = new Definition();
    public final class Definition extends Property<String> implements FromAny, ToOne {
      private Definition() { super(String.class); }
    }

    public final Category category = new Category();
    public final class Category extends Property<KeywordCategories> implements FromAny, ToOne {
      private Category() { super(KeywordCategories.class); }
    }
  }

  /*
    ### Keyword categories

    Right now keyword categories are represented as an enum. There is a hierarchy though, similar to that found in GO, but with not a clear meaning. Given enough interest, keyword categories can be upgraded to vertices, with an edge representing this hierarchy.
  */
  public static enum KeywordCategories {

    biologicalProcess,
    cellularComponent,
    codingSequenceDiversity,
    developmentalStage,
    disease,
    domain,
    ligand,
    molecularFunction,
    PTM,
    technicalTerm;
  }

  /*
    ### Protein keywords

    An edge etc etc.
  */
  public final class Keywords extends Edge<Protein, Keywords, Keyword> {
    private Keywords(E edge) { super(edge, keywords); }
    @Override public final Keywords self() { return this; }
  }
  public final KeywordsType keywords = new KeywordsType();
  public final class KeywordsType extends EdgeType<Protein, Keywords, Keyword> implements FromAtLeastOne, ToAny {
    private KeywordsType() { super(protein, keyword); }
    @Override public final Keywords fromRaw(E edge) { return new Keywords(edge); }
  }

  /*
    ### Annotations (Features)

    ...
  */
  public final class Annotation extends Vertex<Annotation> {
    private Annotation(V vertex) { super(vertex, annotation); }
    @Override public final Annotation self() { return this; }
  }

  public final AnnotationType annotation = new AnnotationType();
  public final class AnnotationType extends VertexType<Annotation> {

    @Override public final Annotation fromRaw(V vertex) { return new Annotation(vertex); }

    public final FeatureType featureType = new FeatureType();
    public final class FeatureType extends Property<FeatureTypes> implements FromAtLeastOne, ToOne {
      private FeatureType() { super(FeatureTypes.class); }
    }
  }

  public final class Annotations extends Edge<Protein, Annotations, Annotation> {
    private Annotations(E edge) { super(edge, annotations); }
    @Override public final Annotations self() { return this; }
  }

  public final AnnotationsType annotations = new AnnotationsType();
  public final class AnnotationsType extends EdgeType<Protein, Annotations, Annotation> implements FromAtLeastOne, ToAny {
    private AnnotationsType() { super(protein, annotation); }
    @Override public final Annotations fromRaw(E edge) { return new Annotations(edge); }

    /*
      Note that these properties are here in the edge because of the limitations of current graph databases; they should be part of the annotation vertex.

      ##### UniProt data and this

      1. Everything here is **0-based**, with `[start, end[` intervals
      2. Those `<`, `>` symbols for positions are translated to `0` and `length`
      3. uncertain endpoints are properties of the annotation vertex, if anything
    */
    public final Begin begin = new Begin();
    public final class Begin extends Property<Integer> implements FromAny, ToOne {
      private Begin() { super(Integer.class); }
    }

    public final End end = new End();
    public final class End extends Property<Integer> implements FromAny, ToOne {
      private End() { super(Integer.class); }
    }
  }

  /*
    ### Feature types

    They are an enum. As for keywords, they can be upgraded to vertices.
  */
  public static enum FeatureTypes {

    activeSite,
    bindingSite,
    calciumBindingRegion,
    chain,
    coiledCoilRegion,
    compositionallyBiasedRegion,
    crosslink,
    disulfideBond,
    DNABindingRegion,
    domain,
    glycosylationSite,
    helix,
    initiatorMethionine,
    lipidMoietyBindingRegion,
    metalIonBindingSite,
    modifiedResidue,
    mutagenesisSite,
    nonConsecutiveResidues,
    nonTerminalResidue,
    nucleotidePhosphateBindingRegion,
    peptide,
    propeptide,
    regionOfInterest,
    repeat,
    nonstandardAminoAcid,
    sequenceConflict,
    sequenceVariant,
    shortSequenceMotif,
    signalPeptide,
    site,
    spliceVariant,
    strand,
    topologicalDomain,
    transitPeptide,
    transmembraneRegion,
    turn,
    unsureResidue,
    zincFingerRegion,
    intramembraneRegion;
  }

  /*
    ### General annotations (Comments)

    See

    1. http://www.uniprot.org/help/general_annotation
    2. http://web.expasy.org/docs/userman.html#CC_line

    Most comments are just a category together with text; these correspond to the comment vertex here. Whenever comments have some structure, they are promoted to vertices with edges going from proteins to them.
  */
  public final class Comment extends Vertex<Comment> {
    private Comment(V vertex) { super(vertex, comment); }
    @Override public final Comment self() { return this; }
  }

  public final CommentType comment = new CommentType();
  public final class CommentType extends VertexType<Comment> {

    @Override public final Comment fromRaw(V vertex) { return new Comment(vertex); }

    /*
      ### Topic

      The topic, or type, of this comment. Note that the possible values are restricted to those which are not promoted to specific vertex types.
    */
    public final Topic topic = new Topic();
    public final class Topic extends Property<CommentTopics> implements FromAtLeastOne, ToOne {
      private Topic() { super(CommentTopics.class); }
    }

    /*
      #### Text

      This property contains the text of this comment.
    */
    public final Text text = new Text();
    public final class Text extends Property<String> implements FromAny, ToOne {
      private Text() { super(String.class); }
    }
  }

  /*
    This enum only contains those topics which do *not* give rise to specific vertex types.
  */
  public static enum CommentTopics {

    allergen,
    alternativeProducts, // TODO remove, isoforms
    biotechnology,
    biophysicochemicalProperties,
    catalyticActivity,
    caution,
    cofactor,
    developmentalStage,
    disease,
    domain,
    disruptionPhenotype,
    enzymeRegulation,
    function,
    induction,
    miscellaneous,
    pathway,
    pharmaceutical,
    polymorphism,
    PTM,
    RNAEditing,
    similarity,
    subcellularLocation,
    sequenceCaution,
    subunit,
    tissueSpecificity,
    toxicDose,
    onlineInformation,
    massSpectrometry,
    interaction;
  }

  /*
    ### Isoforms

    See

    1. http://www.uniprot.org/help/canonical_and_isoforms
    2. http://www.uniprot.org/help/alternative_products

    The role of isoforms in UniProt data is extremely confusing and bad designed. We are doing the best we can here which accommodates existing data: there are protein vertices, connected between themselves through isoform edges. Proteins (which are actually UniProt entries) are connected with their canonical protein product through this edge. It is important to note that most of the data from the entry actually refers to this canonical protein product.

    The isoform relation in UniProt is not transitive; if you really want to get all isoforms of a protein you need to compute the transitive closure of this edge.
  */
  public final class Isoforms extends Edge<Protein, Isoforms, Protein> {
    private Isoforms(E edge) { super(edge, isoforms); }
    @Override public final Isoforms self() { return this; }
  }

  public final IsoformsType isoforms = new IsoformsType();
  public final class IsoformsType extends EdgeType<Protein, Isoforms, Protein> implements FromAny, ToAny {
    private IsoformsType() { super(protein, protein); }
    @Override public final Isoforms fromRaw(E edge) { return new Isoforms(edge); }
  }
}
