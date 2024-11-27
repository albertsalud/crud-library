# crud-library
## Summary
Library for supply basic crud operations on REST APIs.

Library is a Java 17, spring based project (spring web, spring data and spring validation). It provides a suite of tested, light-weight classes at all information system layers. You will found an implementation example on my [PhoneBook project repository](https://google.es)

## Operations
CRUD library supply a basic implementation for the following operations:
- Creating a domain entity.
- Accessing a domain entity by id.
- Updating a domain entity.
- Deleting a domain entity.
- Getting a list of domain entities (all of them).
- Getting a paginated list of domain entities (all of them).

## Project classes
### CrudRepository<D,I> (interface)
This interface provides basic operations to access persistence layer. Its methods matches with Spring data JPARepository interface to help integration with this approach, but allowing to choose another one if desired.
- D is the domain Class.
- I is the attribute type containing the domain id.

e.g. public interface PersonRepository extends CrudRepository<Person, Long>, on Person is the domain class, and Long is the domain class identity field type.
* Note: Domain class id field/method must be annotated with jakarta.persistence.@Id annotation.

### CrudService<D, I> (abstract class)
This abstract class provides basic CRUD services for the designed domain entity, as follows:
- find by id: returns an Optional domain object. Throws CrudException if no id is provided (null).
- find all: returns a list of existing domain objects.
- create: adds a new object to persistence context. Throws CrudException if no object is provided (null).
- update: updates an existeng object. Throws CrudException whether:
  - no domain object is provided (null).
  - no id provided in domain object.
  - unexisting id at database.
- delete: deletes an existing object by id. Throws CrudException if:
  - no id is provided (null).
  - unexisting id at database.
- find all (paginated): returns a paginated list of existing domain objects.

e.g. public class PersonService extends CrudService<Person, Long>, on Person is the domain class, and Long is the domain class identity field type.

You can easily change the default service behavior by overriding any parent method, or using a Decorator pattern, if necessary.

### CrudController<D, B, I> (abstract class)







  
