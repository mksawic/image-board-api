# ImageBoardAPI

https://mksawic-imageboard-api.herokuapp.com/
  
This project is try-copy application of websites like 9gag, imgur etc. It's created completely by me in purpose of learning full-stack web development. 
I'm using Spring Boot to comunicate with MongoDB Atlas Cloud where all data is stored. On the frontside I'm using Angular 9 application 
(https://github.com/mksawic/image-board-client)
 
## Release v1.0.0

### Features
* Basic authentication system
* Uploading and deleting posts
* Voting system
* Overview of posts from specific user.

### Usage
| Method |         URL         |                   Body request                  |                                      Action                                     |
|:------:|:-------------------:|:-----------------------------------------------:|:-------------------------------------------------------------------------------:|
|   GET  |      /api/posts     |           page: int <br>username?: string       |      Return JSON posts list. page=0 for main page. Username is not required     |
|   GET  |      /api/post      |                    id: string                   |                       Return Post object with specific ID.                      |
|   GET  |  /api/posts/voteUp  |           id: string <br>authToken: string      |    Vote up for post with passed id. Return HTTP.OK when authToken is correct    |
|   GET  | /api/posts/voteDown |           id: string <br>authToken: string      |   Vote down for post with passed id. Return HTTP.OK when authToken is correct   |
|  POST  |     /api/posts/     |title: string <br>image: file <br>authToken: string | Post uploading. Image is a file type. Return HTTP.OK when authToken is correct  |
| DELETE |      /api/posts     |           id: string <br>authToken: string          |             Post deleting. Return HTTP.OK when authToken is correct    |
|   GET  |   /api/auth/signin  |        username: string <br>password: string        |  If user nad password are correct it return response body with all user details. |
|   GET  |   /api/auth/signup  | username: string <br>email: string <br>password: string |                   Return HTTP.OK if all credentials are okay.                   |

### Incoming features
* Email verification and password reset
* Better comunication with API.
* Tags system
* Search and sorting system

### Overview
![alt text](https://i.imgur.com/dqv5u75.png)

## License
[MIT](https://choosealicense.com/licenses/mit/)

