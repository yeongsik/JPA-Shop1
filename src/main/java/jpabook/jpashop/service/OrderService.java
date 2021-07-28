package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    /*
     * 주문
     * */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 설정
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member,delivery , orderItem);

        // createOrder 외에 다른 생성 스타일을 막아야 한다.
        // 일반적인 생성자를 제약하는 형태로 만들자 -> 각 엔티티에 @NoArgsConstructor(access = AccessLevel.PROTECTED)
        // 코드는 제약하는 스타일로 짜야한다.
        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /*
     * 주문 취소
     * */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();
        // 주문 엔티티 자체에 cancel 로직이 있다.
        // 여기서 JPA 강점이 나타난다.
        // 원래는 직접 update 쿼리를 날려서 짜야 한다.
        // JPA를 활용하면 엔티티 데이터만 바꿔도 더티 체킹 ( 변경 내역 감지 가 일어나면서
        // 데이터베이스에 업데이트 쿼리가 날라간다.
    }

    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
