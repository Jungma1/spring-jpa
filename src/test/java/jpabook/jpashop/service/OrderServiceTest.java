package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("상품 주문")
    void createOrder() {
        // given
        Member member = createMember();
        Item book = createBook("JPA Basic", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(10000 * orderCount).isEqualTo(findOrder.getTotalPrice());
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("상품 주문 재고가 없을 시 예외 발생")
    void overItemStock() {
        Member member = createMember();
        Item item = createBook("JPA Basic", 10000, 10);

        int orderCount = 11;

        assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    @DisplayName("주문 취소")
    void cancelOrder() {
        // given
        Member member = createMember();
        Item item = createBook("JPA Basic", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(item.getStockQuantity()).isEqualTo(10);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("Member1");
        member.setAddress(new Address("city", "street", "10000"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}
