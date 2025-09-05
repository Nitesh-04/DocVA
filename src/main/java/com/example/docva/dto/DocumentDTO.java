package com.example.docva.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DocumentDTO (Long id, String fileName, String owner, String fileLink, LocalDateTime createdAt, List<VersionDTO> versions){}


// DTO Job ->

//for example for audit a query will return
//        {
//        "content": [
//        {
    //        "id": 1,
    //        "username": "user1",
    //        "action": "UPLOAD",
    //        "timestamp": "2025-09-05T22:00:00",
    //        "document": {
        //        "id": 10,
        //        "fileName": "report.pdf",
        //        "owner": "user1",
        //        "fileLink": "/files/report_v1.pdf",
        //        "versions": [
        //              { "id": 1, "versionNo": 1, "versionLink": "/files/report_v1.pdf" }
//                  ]
//              }
//          }
//        ],
    //        "pageable": { ... },
    //        "totalElements": 1
//        }
//
//
//
//this has document and version
//
//        using a DTO we can get a result like
//
//{
//    "content": [
//    {
//        "id": 1,
//            "username": "user1",
//            "action": "UPLOAD",
//            "timestamp": "2025-09-05T22:00:00",
//            "documentId": 10
//    }
//  ],
//    "pageable": { ... },
//    "totalElements": 1
//}
