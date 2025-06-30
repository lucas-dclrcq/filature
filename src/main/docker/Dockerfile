FROM mcr.microsoft.com/playwright:v1.53.2-noble

WORKDIR /work/

RUN chown 1001:root /work \
    && chmod g+rwX /work \
    && chown 1001:root /work

COPY --chown=1001:root target/*.properties target/*.so /work/
COPY --chown=1001:root target/*-runner /work/application

RUN chmod ugo+x /work/application

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]