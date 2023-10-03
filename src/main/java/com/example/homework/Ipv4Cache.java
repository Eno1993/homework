package com.example.homework;

import com.example.homework.config.PacketDto;
import org.pcap4j.packet.IpV4Packet;

import org.pcap4j.packet.TcpPacket;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Ipv4Cache {

    private static final Queue<PacketDto.PacketData> ipV4PacketQueue = new LinkedList<>();
    private static final Map<String, PacketDto.PacketFragment> fragmentMap = new HashMap<>();


    public void addPacket(IpV4Packet ipV4Packet){

        if(ipV4Packet.getHeader().getDontFragmentFlag()){
            ipV4PacketQueue.offer(new PacketDto.PacketData(ipV4Packet, Optional.empty()));
        }else{
            packetGather(ipV4Packet);
        }
    }

    public void packetGather(IpV4Packet ipV4Packet){

        IpV4Packet.IpV4Header ipV4Header = ipV4Packet.getHeader();
        Optional<TcpPacket> tcpPacket = Optional.ofNullable(ipV4Packet.get(TcpPacket.class));

        if(tcpPacket.isPresent()){
            TcpPacket.TcpHeader tcpHeader = tcpPacket.get().getHeader();
            String key = getHashCode(ipV4Header.getSrcAddr().toString(),
                    ipV4Header.getDstAddr().toString(),
                    tcpHeader.getSrcPort().toString(),
                    tcpHeader.getDstPort().toString());

            fragmentMap.computeIfAbsent(key, value -> new PacketDto.PacketFragment()).update(ipV4Packet);

            fragmentMap.get(key).gatherPacket().ifPresent(packet -> {
                ipV4PacketQueue.offer(packet);
                fragmentMap.remove(key);
            });
        }

    }


    public Optional<PacketDto.PacketData> getPacket(){
        if(!ipV4PacketQueue.isEmpty()){
            return Optional.of(ipV4PacketQueue.poll());
        }
        return Optional.empty();
    }


    public String getHashCode(String ... inputs){
        return String.join("", inputs);
    }



}
