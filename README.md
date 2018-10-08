# spring-boot-datajpatest
Hi all, I tried to test my repository with the `@DataJpaTest` annotation but something strange appears.

When I use a classic `@GeneratedValue` every thing is OK, my test succeed. But when I use the generator bellow my test failed.

The test `createCountry_should_succeed` succeed but the others don't because no exceptions on Valitation on constraint are throw.
```java
@GeneratedValue(generator = "UUID")
@GenericGenerator(name = "UUID", strategy = "com.example.demojpa.CustomIdentifierGenerator") 
```
For exemple here one of the failed assertion : 

```
java.lang.AssertionError: Expected test to throw an instance of org.springframework.dao.DataIntegrityViolationException
	at org.junit.Assert.fail(Assert.java:88)
	at org.junit.rules.ExpectedException.failDueToMissingException(ExpectedException.java:263)
	at org.junit.rules.ExpectedException.access$200(ExpectedException.java:106)
	at org.junit.rules.ExpectedException$ExpectedExceptionStatement.evaluate(ExpectedException.java:245)
	at org.junit.rules.RunRules.evaluate(RunRules.java:20)
	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:252)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:94)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:191)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
```

### My Entity class
```java
@NoArgsConstructor
@Data
@Entity()
@Table(name = "mw_ecom_country", uniqueConstraints = {@UniqueConstraint(name = "abbreviation", columnNames = "abbreviation")})
public class Country {

    @Id
    @GeneratedValue
    //@GeneratedValue(generator = "UUID")
    //@GenericGenerator(name = "UUID", strategy = "com.example.demojpa.CustomIdentifierGenerator")
    protected Long id;

    @NotNull
    @NotEmpty
    private String abbreviation;

    @NotNull
    private String name;

    public Country(Long id, String abbreviation, String name) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
    }
}
```
### Here is the test
    
```java
@DataJpaTest
@RunWith(SpringRunner.class)
public class CountryRepoTest {

    @Autowired
    private CountryRepository countryRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createCountry_should_succeed() {
        Country country = countryRepository.save(new Country(null, "FR", "France"));
        assertThat(country.getId(), notNullValue());
    }

    @Test
    public void createCountry_should_failed_duplicate_abbreviation() {
        exception.expect(DataIntegrityViolationException.class);
        countryRepository.save(new Country(null, "FR", "France"));
        countryRepository.save(new Country(null, "FR", "France"));
    }

    @Test
    public void createCountry_should_failed_null_abbreviation() {
        exception.expect(ConstraintViolationException.class);
        countryRepository.save(new Country(null, null, "France"));
    }

    @Test
    public void createCountry_should_failed_empty_abbreviation() {
        exception.expect(ConstraintViolationException.class);
        countryRepository.save(new Country(null, "", "France"));

    }
}
```
### And the Custom IdentifierGenerator
```java
public class CustomIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return new Random().nextLong();
    }
}
```