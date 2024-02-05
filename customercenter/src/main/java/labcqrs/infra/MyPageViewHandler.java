package labcqrs.infra;

import java.util.Optional;
import labcqrs.config.kafka.KafkaProcessor;
import labcqrs.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MyPageViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderPlaced_then_CREATE_1(
        @Payload OrderPlaced orderPlaced
    ) {
        try {
            if (!orderPlaced.validate()) return;

            // view 객체 생성
            MyPage myPage = new MyPage();
            myPage.setOrderId(orderPlaced.getId());
            myPage.setCustomerId(orderPlaced.getCustomerId());
            myPage.setProductId(orderPlaced.getProductId());
            myPage.setQty(orderPlaced.getQty());
            myPage.setOrderStatus(orderPlaced.getStatus());
            myPageRepository.save(myPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_1(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findByOrderId(deliveryStarted.getOrderId());

            if( myPageOptional.isPresent()) {
                MyPage myPage = myPageOptional.get();
                myPage.setDeliveryStatus(deliveryStarted.getStatus());    
                myPageRepository.save(myPage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
