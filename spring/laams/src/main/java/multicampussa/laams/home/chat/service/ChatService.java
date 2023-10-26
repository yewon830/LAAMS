package multicampussa.laams.home.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import multicampussa.laams.director.domain.Director;
import multicampussa.laams.home.chat.domain.ChatRoom;
import multicampussa.laams.home.chat.dto.CreateChatRoomDto;
import multicampussa.laams.home.chat.repository.ChatRepository;
import multicampussa.laams.home.member.dto.MemberSignUpDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<String, ChatRoom> chatRooms;
    private final ChatRepository chatRepository;

    @PostConstruct
    //의존관계 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = chatRepository.findAll();
        Collections.reverse(result);

        return result;
    }

    //채팅방 하나 불러오기
    public ChatRoom findByRoomName(String roomName) {
        return chatRepository.findByRoomName(roomName);
    }

    //채팅방 생성
    public ResponseEntity<String> createRoom(CreateChatRoomDto createChatRoomDto) {
        String roomName = createChatRoomDto.getDirectorId() + "&" + createChatRoomDto.getManagerId();
        if (chatRepository.existsByRoomName(roomName)) {
            return ResponseEntity.status(HttpStatus.valueOf(10001)).body("존재하는 채팅방입니다.");
        }

        ChatRoom chatRoom = ChatRoom.create(roomName);
        chatRepository.save(chatRoom);
        return ResponseEntity.status(HttpStatus.OK).body("채팅방 개설에 성공하였습니다.");
    }
}