package com.example.homework;

import org.pcap4j.packet.IpV4Packet;

import org.springframework.stereotype.Component;

@Component
public class Ipv4Cache {





    public void packetParsing(IpV4Packet ipV4Packet){
        IpV4Packet.IpV4Header ipV4Header = ipV4Packet.getHeader();

        boolean moreFragment = ipV4Header.getMoreFragmentFlag();
        boolean dontFragment = ipV4Header.getDontFragmentFlag();


        

    }




}
