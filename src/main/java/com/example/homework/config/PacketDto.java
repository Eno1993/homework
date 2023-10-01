package com.example.homework.config;

import lombok.NoArgsConstructor;
import org.pcap4j.packet.IpV4Packet;

import java.util.Optional;
import java.util.PriorityQueue;

public class PacketDto {

    public static class PacketData{
        private final IpV4Packet ipV4Packet;
        private final Optional<byte[]> rawData;

        public PacketData(IpV4Packet ipV4Packet, Optional<byte[]> rawData){
            this.ipV4Packet = ipV4Packet;
            this.rawData = rawData;
        }
    }


    @NoArgsConstructor
    public static class PacketFragment{
        private int totalLength;

        private final PriorityQueue<IpV4Packet> pq = new PriorityQueue<>(
                (p1, p2) -> p1.getHeader().getFragmentOffset() < p2.getHeader().getFragmentOffset() ? -1 : 1);

        public Optional<PacketData> update(IpV4Packet ipV4Packet){


            //fixme 이 부분 수정 필요.

            return Optional.empty();
        }

    }

}
