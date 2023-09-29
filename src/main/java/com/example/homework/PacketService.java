package com.example.homework;

import lombok.RequiredArgsConstructor;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PacketService {

    private final Ipv4Cache ipv4Cache;

    public void parsingPacket(Packet packet){
        if(packet.contains(IpV4Packet.class)){
            ipv4Cache.packetParsing(packet.get(IpV4Packet.class));
        }
    }


}
