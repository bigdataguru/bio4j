package com.bio4j.model.uniprot.relationships.book;

import com.bio4j.model.Relationship;
import com.bio4j.model.RelationshipType;
import com.bio4j.model.RelationshipType.Arity;
import com.bio4j.model.uniprot.nodes.Publisher;
import com.bio4j.model.uniprot.nodes.Book;

/**
 *
 * @author Pablo Pareja <ppareja@era7.com>
 */
public interface BookPublisher extends Relationship <
  Book, Book.Type,
  BookPublisher, BookPublisher.Type,
  Publisher, Publisher.Type
> {

  enum Type implements RelationshipType <
    Book, Book.Type,
    BookPublisher, BookPublisher.Type,
    Publisher, Publisher.Type
  > {
    bookPublisher;
    public Type value() { return bookPublisher; }
    public Arity arity() { return Arity.manyToOne; }
    public Book.Type sourceType() { return Book.TYPE; }
    public Publisher.type targetType() { return Publisher.TYPE; }

  }

}
