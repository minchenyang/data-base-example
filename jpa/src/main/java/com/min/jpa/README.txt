JPA
JPA规范本质上就是一种ORM规范，它只是制订了一些规范，提供了一些编程的API接口，但具体实现则由服务厂商来提供实现。

Spring Data JPA
Spring Data JPA 是在JPA规范的基础下提供了Repository层的实现，但是使用那一款ORM需要你自己去决定。

注解
@Entity	            声明类为实体或表。
@Table	            声明表名。
@Basic	            指定非约束明确的各个字段。
@Embedded	        指定类或它的值是一个可嵌入的类的实例的实体的属性。
@Id	                指定的类的属性，用于识别（一个表中的主键）。
@GeneratedValue	    指定如何标识属性可以被初始化，例如自动、手动、或从序列表中获得的值。
@Transient	        指定的属性，它是不持久的，即：该值永远不会存储在数据库中。
@Column	            指定持久属性栏属性。
@SequenceGenerator	指定在@GeneratedValue注解中指定的属性的值。它创建了一个序列。
@TableGenerator	    指定在@GeneratedValue批注指定属性的值发生器。它创造了的值生成的表。
@AccessType	        这种类型的注释用于设置访问类型。如果设置@AccessType（FIELD），则可以直接访问变量并且不需要getter和setter，但必须为public。如果设置@AccessType（PROPERTY），通过getter和setter方法访问Entity的变量。
@JoinColumn	        指定一个实体组织或实体的集合。这是用在多对一和一对多关联。
@UniqueConstraint	指定的字段和用于主要或辅助表的唯一约束。
@ColumnResult	    参考使用select子句的SQL查询中的列名。
@ManyToMany	        定义了连接表之间的多对多一对多的关系。
@ManyToOne	        定义了连接表之间的多对一的关系。
@OneToMany	        定义了连接表之间存在一个一对多的关系。
@OneToOne	        定义了连接表之间有一个一对一的关系。
@NamedQueries	    指定命名查询的列表。
@NamedQuery	        指定使用静态名称的查询。

//文档
https://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#specifications