# Database Images

On this section we hold the database schema. For each database we build an image in which we mount the `.sql` representation of each schema. For more information about each database please visit the corresponding folder.

Once you build & deploy a database container (for the first time) you should navigate to the `/docker-entrypoint-initdb.d`, check that the schema exists and execute the appropriate script. Please check the corresponding Dockerfile whith which we create each database image. 


