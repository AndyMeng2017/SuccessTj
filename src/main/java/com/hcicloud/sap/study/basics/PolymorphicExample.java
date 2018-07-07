package com.hcicloud.sap.study.basics;

import org.junit.Test;

/**
 * 1.在继承链中对象方法的调用存在一个优先级：this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)。
 * 2.指向子类的父类引用由于向上转型了，它只能访问父类中拥有的方法和属性，而对于子类中存在而父类中不存在的方法，该引用是不能使用的，尽管是重载该方法。
 *   若子类重写了父类中的某些方法，在调用该些方法的时候，必定是使用子类中定义的这些方法（动态连接、动态调用）
 */
public class PolymorphicExample {

    public class A {
        public String show(D obj) {
            return ("A and D");
        }

        public String show(A obj) {
            return ("A and A");
        }

    }

    public class B extends A{
        public String show(B obj){
            return ("B and B");
        }

        public String show(A obj){
            return ("B and A");
        }
    }

    public class C extends B{

    }

    public class D extends B{

    }

    @Test
    public void test() {
        A a1 = new A();
        A a2 = new B();
        B b = new B();
        C c = new C();
        D d = new D();

        System.out.println("1--" + a1.show(b));
        System.out.println("2--" + a1.show(c));
        System.out.println("3--" + a1.show(d));

//        解释下：
//        1.首先a2是A引用，B实例，调用show（B b）方法，此方法在父类A中没有定义，所以B中方法show(B b)不会调用（多态必须父类中已定义该方法）
//        2.this.show(O) 发现没有，就是去执行A类中的show(B)，没有，继续向上
//        3.super.show(O) A类没有继承，没有，
//        4.this.show((super)O) 这时要执行A类中的show(A)方法，但是因为B类继承A类，同时覆写show(A)方法，于是执行的就是B类中的show(A)
//        得出 B and A
        System.out.println("4--" + a2.show(b));


        System.out.println("5--" + a2.show(c));  //同上
        System.out.println("6--" + a2.show(d));  //A and D .查找B中没有show(D d)方法，再查A中，有，执行。
        System.out.println("7--" + b.show(b));
        System.out.println("8--" + b.show(c));  //B and B .
        System.out.println("9--" + b.show(d));
    }

}
