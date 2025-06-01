# Spring Data JPA Cheat Sheet

## Repository Basics

### 1. Core Repository Interfaces
```java
public interface MyRepository extends JpaRepository<Entity, ID> {}
public interface MyRepository extends CrudRepository<Entity, ID> {}
public interface MyRepository extends PagingAndSortingRepository<Entity, ID> {}
```

### 2. Common Methods (Automatically Implemented)
```java
save(S entity)                 // Save entity
saveAll(Iterable<S> entities)  // Save multiple entities
findById(ID id)                // Find by primary key
existsById(ID id)              // Check if exists
findAll()                      // Get all entities
count()                        // Count all entities
deleteById(ID id)              // Delete by ID
delete(T entity)               // Delete entity
deleteAll()                    // Delete all entities
```

## Query Method Keywords

### 1. Basic Query Structure
```
[action]By[property][condition][And|Or][property][condition]...
```

### 2. Common Keywords

| Keyword            | Example                                 | JPQL Equivalent                                              |
|--------------------|-----------------------------------------|--------------------------------------------------------------|
| And                | `findByLastnameAndFirstname`            | `where x.lastname = ?1 and x.firstname = ?2`                   |
| Or                 | `findByLastnameOrFirstname`             | `where x.lastname = ?1 or x.firstname = ?2`                    |
| Is, Equals         | `findByFirstname`, `findByFirstnameIs`  | `where x.firstname = ?1`                                       |
| Between            | `findByStartDateBetween`                | `where x.startDate between ?1 and ?2`                         |
| LessThan           | `findByAgeLessThan`                     | `where x.age < ?1`                                             |
| LessThanEqual      | `findByAgeLessThanEqual`                | `where x.age <= ?1`                                            |
| GreaterThan        | `findByAgeGreaterThan`                  | `where x.age > ?1`                                             |
| GreaterThanEqual   | `findByAgeGreaterThanEqual`             | `where x.age >= ?1`                                            |
| After              | `findByStartDateAfter`                  | `where x.startDate > ?1`                                       |
| Before             | `findByStartDateBefore`                 | `where x.startDate < ?1`                                       |
| IsNull             | `findByAgeIsNull`                        | `where x.age is null`                                          |
| IsNotNull, NotNull | `findByAgeIsNotNull`                     | `where x.age not null`                                         |
| Like               | `findByFirstnameLike`                   | `where x.firstname like ?1`                                     |
| NotLike            | `findByFirstnameNotLike`                | `where x.firstname not like ?1`                                |
| StartingWith       | `findByFirstnameStartingWith`           | `where x.firstname like ?1%`                                    |
| EndingWith         | `findByFirstnameEndingWith`             | `where x.firstname like %?1`                                    |
| Containing         | `findByFirstnameContaining`             | `where x.firstname like %?1%`                                    |
| OrderBy            | `findByAgeOrderByLastnameDesc`          | `where x.age = ?1 order by x.lastname desc`                    |
| Not                | `findByLastnameNot`                     | `where x.lastname <> ?1`                                       |
| In                 | `findByAgeIn(Collection<Age> ages)`     | `where x.age in ?1`                                            |
| NotIn              | `findByAgeNotIn(Collection<Age> ages)`  | `where x.age not in ?1`                                        |
| True               | `findByActiveTrue()`                     | `where x.active = true`                                        |
| False              | `findByActiveFalse()`                    | `where x.active = false`                                       |
| IgnoreCase         | `findByFirstnameIgnoreCase`             | `where UPPER(x.firstname) = UPPER(?1)`                         |

### 3. Special Operations
| Keyword      | Example                              | Description             |
|--------------|--------------------------------------|-------------------------|
| countBy      | `countByLastname(String lastname)`   | Count entities          |
| deleteBy, removeBy | `deleteByLastname(String lastname)` | Delete entities       |
| existsBy     | `existsByEmail(String email)`        | Check if exists         |
| findFirst, findTop | `findFirst5ByLastname(String lastname)` | Limit results     |

## Custom Queries

### 1. @Query Annotation
```java
@Query("SELECT p FROM Patient p WHERE p.email = ?1 AND p.active = true")
Patient findActivePatientByEmail(String email);

// Native SQL query
@Query(value = "SELECT * FROM patients WHERE email = ?1", nativeQuery = true)
Patient findByEmailNative(String email);
```

### 2. Named Parameters
```java
@Query("SELECT p FROM Patient p WHERE p.email = :email AND p.dob > :date")
List<Patient> findByEmailAndDobAfter(@Param("email") String email, 
                                    @Param("date") LocalDate date);
```

## Pagination and Sorting
```java
Page<Patient> findByLastname(String lastname, Pageable pageable);
Slice<Patient> findByFirstname(String firstname, Pageable pageable);
List<Patient> findByEmail(String email, Sort sort);

// Usage example:
Page<Patient> patients = patientRepository.findByLastname(
    "Smith", 
    PageRequest.of(0, 10, Sort.by("firstname").ascending())
);
```

## Entity Basics

### 1. Common Annotations
```java
@Entity
@Table(name = "patients")  // Optional table name
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or UUID, SEQUENCE, etc.
    private Long id;
    
    @Column(name = "full_name", nullable = false, length = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Transient  // Not persisted
    private Integer age;
    
    // Getters and setters
}
```

### 2. Relationships
```java
// One-to-Many
@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
private List<Appointment> appointments;

// Many-to-One
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "doctor_id")
private Doctor doctor;

// Many-to-Many
@ManyToMany
@JoinTable(name = "patient_allergies",
    joinColumns = @JoinColumn(name = "patient_id"),
    inverseJoinColumns = @JoinColumn(name = "allergy_id"))
private Set<Allergy> allergies;
```

## Transaction Management
```java
@Transactional
public void updatePatientDetails(Patient patient) {
    // Multiple repository operations in single transaction
    patientRepository.save(patient);
    auditRepository.logUpdate(patient.getId());
}
```

## Common Patterns

### 1. Soft Delete
```java
@Entity
public class Patient {
    // ...
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}

// In repository:
@Query("SELECT p FROM Patient p WHERE p.isDeleted = false")
List<Patient> findAllActive();

@Modifying
@Query("UPDATE Patient p SET p.isDeleted = true WHERE p.id = ?1")
void softDelete(Long id);
```

### 2. Auditing
```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Patient {
    // ...
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String modifiedBy;
}

// Enable in config:
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("currentUser");
    }
}
```

## Performance Tips
- Use `FetchType.LAZY` for relationships
- For read-only operations, annotate methods with `@Transactional(readOnly = true)`
- Consider `@EntityGraph` for eager loading when necessary
- Use DTO projections instead of full entities when appropriate
- For bulk operations, use `@Modifying` with custom queries

---

*Keep this cheat sheet handy while developing Spring Data JPA repositories!*

