package com.example.homework.config;

import lombok.NoArgsConstructor;
import org.pcap4j.packet.IpV4Packet;

import java.util.HashMap;
import java.util.Map;
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

        private int totalLength = 0;
        private IpV4Packet ipV4Packet;
        private final Map<Short, Boolean> fragmentMap = new HashMap<>();
        private final PriorityQueue<IpV4Packet> pq = new PriorityQueue<>(
                (p1, p2) -> p1.getHeader().getFragmentOffset() <= p2.getHeader().getFragmentOffset() ? -1 : 1);



        public void update(IpV4Packet ipV4Packet){

            pq.offer(ipV4Packet);
            totalLength += ipV4Packet.getHeader().getTotalLengthAsInt();

            short offSet = ipV4Packet.getHeader().getFragmentOffset();
            fragmentMap.put(offSet, true);
            if(offSet==0){
                this.ipV4Packet = ipV4Packet;
            }

            if(ipV4Packet.getHeader().getMoreFragmentFlag()){
                int nextOffSet = ipV4Packet.getHeader().getTotalLengthAsInt()-ipV4Packet.getHeader().getIhl();
                if(!fragmentMap.containsKey((short)nextOffSet)){
                    fragmentMap.put((short)nextOffSet, false);
                }
            }

        }

        public Optional<PacketData> gatherPacket(){
            boolean check = fragmentMap.entrySet().stream()
                    .allMatch(entry -> entry.getValue().equals(true));

            if(check){
                // byte 조합해서 다 변환
                byte[] res = new byte[totalLength];
                while(!pq.isEmpty()){
                    IpV4Packet pk = pq.poll();
                    short start = pk.getHeader().getFragmentOffset();
                    System.arraycopy(pk.getRawData(), 0, res, start, pk.getRawData().length);
                }
                PacketData packetData = new PacketData(this.ipV4Packet, Optional.of(res));
                return Optional.of(packetData);
            }else{
                return Optional.empty();
            }
        }

    }

}
