package org.protobeans.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.hibernate.config.HibernateConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.vendor.Database;

/**
 * My hibernate documentation:

@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) <- default fetch type
@Fetch(FetchMode.SELECT) <- default FetchMode
Set<Post> posts = new HashSet<>();

This fetch mode generate following N+1 SQL queries on first collection access:
    select
        user0_.id as id1_3_,
        user0_.name as name2_3_ 
    from
        users user0_

...repeated...
    select
        posts0_.user_id as user_id3_2_0_,
        posts0_.id as id1_2_0_,
        posts0_.id as id1_2_1_,
        posts0_.content as content2_2_1_,
        posts0_.user_id as user_id3_2_1_ 
    from
        posts posts0_ 
    where
        posts0_.user_id=?
...

---------------------------------------------------------------------------

@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) <- default fetch type
@Fetch(FetchMode.JOIN) <- change fetch parameter to FetchType.EAGER and collection fetch EAGERLY!
Set<Post> posts = new HashSet<>();

If parent entity loaded by range (f.e. "SELECT u FROM User u") then 
@Fetch(FetchMode.JOIN) converts to @Fetch(FetchMode.SELECT)

If parent entity loaded by id (f.e. em.find(User.class, id)) then
@Fetch(FetchMode.JOIN) generate following JOIN query:
    select
        user0_.id as id1_3_0_,
        user0_.name as name2_3_0_,
        posts1_.user_id as user_id3_2_1_,
        posts1_.id as id1_2_1_,
        posts1_.id as id1_2_2_,
        posts1_.content as content2_2_2_,
        posts1_.user_id as user_id3_2_2_ 
    from
        users user0_ 
    left outer join
        posts posts1_ 
            on user0_.id=posts1_.user_id 
    where
        user0_.id=?

---------------------------------------------------------------------------

@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) <- default fetch type
@Fetch(FetchMode.SUBSELECT)
Set<Post> posts = new HashSet<>();

If parent entity loaded by range (f.e. "SELECT u FROM User u") then 
@Fetch(FetchMode.SUBSELECT) generate following SUBSELECT query on first collection access:
    select
        user0_.id as id1_3_,
        user0_.name as name2_3_ 
    from
        users user0_

    select
        posts0_.user_id as user_id3_2_1_,
        posts0_.id as id1_2_1_,
        posts0_.id as id1_2_0_,
        posts0_.content as content2_2_0_,
        posts0_.user_id as user_id3_2_0_ 
    from
        posts posts0_ 
    where
        posts0_.user_id in (
            select
                user0_.id 
            from
                users user0_
        )

If parent entity loaded by id (f.e. em.find(User.class, id)) then
@Fetch(FetchMode.SUBSELECT) converts to @Fetch(FetchMode.SELECT)

---------------------------------------------------------------------------

@OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
@Fetch(FetchMode.JOIN) <- default FetchMode
Set<Item> items = new HashSet<>();

Behavior is similar FetchType.LAZY, but collection is eagerly fetched.

---------------------------------------------------------------------------

@ManyToOne(fetch = FetchType.EAGER) <- default fetch type
User user;

From Hibernate in Action book:
The @ManyToOne annotation marks a property as an entity association, and it’s
required. Unfortunately, its fetch parameter defaults to EAGER: this means the associated
Item is loaded whenever the Bid is loaded. We usually prefer lazy loading as a
default strategy, and we’ll talk more about it later in section 12.1.1.

---------------------------------------------------------------------------

@ManyToOne(fetch = FetchType.EAGER)
@Fetch(FetchMode.SELECT)
User user;

This fetch mode generate following N+1 SQL queries:
    select
        post0_.id as id1_2_,
        post0_.content as content2_2_,
        post0_.user_id as user_id3_2_ 
    from
        posts post0_

...repeated...
    select
        user0_.id as id1_3_0_,
        user0_.name as name2_3_0_ 
    from
        users user0_ 
    where
        user0_.id=?
...

---------------------------------------------------------------------------

@ManyToOne(fetch = FetchType.EAGER)
@Fetch(FetchMode.JOIN)<- default FetchMode
User user;

If child entity loaded by range (f.e. "SELECT p FROM Post p") then 
@Fetch(FetchMode.JOIN) converts to @Fetch(FetchMode.SELECT)

If child entity loaded by id (f.e. em.find(Post.class, id)) then
@Fetch(FetchMode.JOIN) generate following JOIN query:

    select
        post0_.id as id1_2_0_,
        post0_.content as content2_2_0_,
        post0_.user_id as user_id3_2_0_,
        user1_.id as id1_3_1_,
        user1_.name as name2_3_1_ 
    from
        posts post0_ 
    left outer join
        users user1_ 
            on post0_.user_id=user1_.id 
    where
        post0_.id=?

---------------------------------------------------------------------------

@ManyToOne(fetch = FetchType.EAGER)
@Fetch(FetchMode.SUBSELECT)
User user;

org.hibernate.AnnotationException: Use of FetchMode.SUBSELECT not allowed on ToOne associations

---------------------------------------------------------------------------

@ManyToOne(fetch = FetchType.LAZY)
@Fetch(FetchMode.SELECT) <- default FetchMode
User user;

Behavior is similar FetchType.EAGER, but entity is lazy fetched.

---------------------------------------------------------------------------

@ManyToOne(fetch = FetchType.LAZY)
@Fetch(FetchMode.JOIN) <- change fetch parameter to FetchType.EAGER
User user;

Behavior is similar FetchType.EAGER

---------------------------------------------------------------------------

@Fetch(FetchMode.SUBSELECT)
org.hibernate.AnnotationException: Use of FetchMode.SUBSELECT not allowed on ToOne associations

---------------------------------------------------------------------------
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HibernateConfig.class)
@Configuration
public @interface EnableHibernate {
    String showSql() default "false";
    
    Database dialect();
    
    /**
     * Also do not forget set log level for org.hibernate.stat to DEBUG
     */
    String enableStatistics() default "false";
        
    String[] basePackages();
    
    int batchSize() default 1_000;
}
