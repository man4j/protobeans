CREATE TABLE public.posts (
  id varchar NOT NULL,
  content varchar NOT NULL,
  created int8 NOT NULL,
  CONSTRAINT posts_pk PRIMARY KEY (id)
);
CREATE INDEX posts_created_idx ON public.posts (created);


CREATE TABLE public.comments (
  id varchar NOT NULL,
  content varchar NOT NULL,
  created int8 NOT NULL,
  post_id varchar NOT NULL,
  CONSTRAINT comments_pk PRIMARY KEY (id)
);
CREATE INDEX comments_created_idx ON public.comments (created);
CREATE INDEX comments_post_id_idx ON public.comments (post_id);