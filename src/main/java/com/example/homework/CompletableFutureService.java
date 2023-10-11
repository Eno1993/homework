package com.example.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureService {


    // todo
    //  completableFuture 의 사용 무게에 대해서 확인해보기
    //  어느정도 무게감이 있을 때 까지 사용할 수 있을까?

    public static void main(String[] args){

        // explain 형태를 가지는지 아닌지에 따라 다르게 분류
        // 1. thenAccept ->
        CompletableFuture<Void> thenAcceptResult = getFuture().thenAccept(value -> {
            System.out.println(value + " : thenAccept execute");
        });



        // 2. thenRun -> 반환값 없이 새로운 쓰레드 에서 작업 실행
        CompletableFuture<Void> thenRunResult = getFuture().thenRun(() -> {
            try{
                System.out.println("thenRun execute");
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        // 3. thenApply -> 반환값 존재
        CompletableFuture<String> thenApplyResult = getFuture()
                .thenApply(value -> value + " : thenApply execute")
                .exceptionally(e -> e.getMessage());


        // 4. thenCombine
        CompletableFuture<String> thenCombineResult =
                getFuture().thenCombine(CompletableFuture.supplyAsync(() -> "thenCombine is return"),
                        (value1, value2) -> value1 + " : " + value2);

        // 5. thenCompose
        CompletableFuture<String> thenComposeResult =
                getFuture().thenCompose(value -> CompletableFuture.supplyAsync(() -> value + ": thenCompose is return"));

        // 6-1. allOf
        CompletableFuture<Void> allOfResult =
                CompletableFuture.allOf(getHello(), getWorld());
        // 6-2. allOf
        /*List<CompletableFuture<String>> list = List.of(getHello(), getWorld());
        CompletableFuture<List<String>> allOfResultReturn =
                CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]))
                        .thenApply(value -> list
                                .stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList()));*/
        //explain 위와 같은 메서드는 비동기 처리를 하는 과정에서 내부적으로는 join 을 호출하여 동기화를 진행하고 있다
        //  따라서 효율적이지 못한 비동기 처리를 진행하고 있기에 비동기 처리가 될 수 있도록 코드를 변경하는 것이 옳다.

        List<Object> resultList = new ArrayList<>();
        CompletableFuture<Void> allOfResultReturn =
                CompletableFuture.allOf(getHello(), getWorld())
                        .thenAccept(resultList::add);

        //이후에 isDone 메서드를 통해 allOfResultReturn 이 완료되었는지 확인 후 resultList 를 꺼내서 확인.


        // 7-1. anyOf
        CompletableFuture<Object> anyOfResult =
                CompletableFuture.anyOf(getHello(), getWorld(), getNumber());


    }

    public static CompletableFuture<String> getFuture(){
        return CompletableFuture.supplyAsync(() -> "future1 is return");
    }
    public static CompletableFuture<String> getHello(){
        return CompletableFuture.supplyAsync(()-> "hello");
    }
    public static CompletableFuture<String> getWorld() {
        return CompletableFuture.supplyAsync(() -> "world");
    }
    public static CompletableFuture<Integer> getNumber(){
        return CompletableFuture.supplyAsync(() -> 10);
    }
}
